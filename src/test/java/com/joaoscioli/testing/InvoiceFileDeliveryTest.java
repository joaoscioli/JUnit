package com.joaoscioli.testing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
