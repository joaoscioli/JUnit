package com.joaoscioli.testing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/** Real filesystem boundary, isolated per test; no mail server or shared paths. */
class InvoiceFileDeliveryTest {
    @TempDir
    Path outbox;

    private InvoiceService service() {
        return new InvoiceService((recipient, subject, body) -> {
            try {
                Files.write(outbox.resolve("invoice.txt"), List.of(recipient, subject, body));
            } catch (IOException failure) {
                throw new UncheckedIOException(failure);
            }
        });
    }

    @Test
    void deliveryCanRecoverAtTheFilesystemBoundaryWithoutReplacingTheService() throws IOException {
        var blocker = outbox.resolve("blocked");
        Files.writeString(blocker, "preserve this file");
        var attempts = new ArrayList<String>();
        var destination = new AtomicReference<>(blocker.resolve("invoice.txt"));
        var invoices = new InvoiceService((recipient, subject, body) -> {
            attempts.add(body);
            try {
                Files.writeString(destination.get(), body);
            } catch (IOException failure) {
                throw new UncheckedIOException(failure);
            }
        });
        var request = new InvoiceRequest("ada@example.com", 1);

        assertThrowsExactly(UncheckedIOException.class, () -> invoices.createInvoice(request));
        assertEquals(List.of("Amount due: 1 cents"), attempts);
        assertEquals("preserve this file", Files.readString(blocker));
        var recovered = outbox.resolve("recovered-invoice.txt");
        assertFalse(Files.exists(recovered));
        destination.set(recovered);

        assertEquals(new Invoice("ada@example.com", 1, "CREATED"), invoices.createInvoice(request));
        assertEquals(List.of("Amount due: 1 cents", "Amount due: 1 cents"), attempts);
        assertEquals("Amount due: 1 cents", Files.readString(recovered));
        assertEquals("preserve this file", Files.readString(blocker));
    }

    @Test
    void writesTheCompleteInvoiceToAnIsolatedOutbox() throws IOException {
        var invoice = service().createInvoice(new InvoiceRequest("ada@example.com", 50_000));

        assertAll(
                () -> assertEquals(new Invoice("ada@example.com", 50_000, "CREATED"), invoice),
                () -> assertEquals(List.of("ada@example.com", "Your invoice is ready", "Amount due: 50000 cents"),
                        Files.readAllLines(outbox.resolve("invoice.txt")))
        );
        try (var files = Files.list(outbox)) {
            assertEquals(1, files.count());
        }
    }

    @Test
    void invalidInvoiceLeavesTheOutboxEmpty() throws IOException {
        assertThrowsExactly(IllegalArgumentException.class,
                () -> service().createInvoice(new InvoiceRequest("ada@example.com", 0)));

        try (var files = Files.list(outbox)) {
            assertEquals(0, files.count());
        }
    }

    @Test
    void filesystemFailurePropagatesWithoutReturningACreatedInvoice() throws IOException {
        // A regular file blocks directory traversal, reliably across operating systems.
        Files.writeString(outbox.resolve("invoice.txt"), "existing delivery");
        outbox = outbox.resolve("invoice.txt");

        var failure = assertThrowsExactly(UncheckedIOException.class,
                () -> service().createInvoice(new InvoiceRequest("ada@example.com", 50_000)));

        assertInstanceOf(IOException.class, failure.getCause());
        assertEquals("existing delivery", Files.readString(outbox));
    }
}
