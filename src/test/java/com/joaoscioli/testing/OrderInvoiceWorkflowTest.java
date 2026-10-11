package com.joaoscioli.testing;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertSame;

/** Component integration with real services and in-memory external boundaries. */
class OrderInvoiceWorkflowTest {
    @Test
    void invoiceDeliveryFailureLeavesTheCompletedPaymentVisibleWithoutAutomaticRetry() {
        var payments = new ArrayList<Long>();
        var emails = new ArrayList<SentEmail>();
        var deliveryFailure = new IllegalStateException("email transport unavailable");
        var orderService = new OrderService((sku, quantity) -> true, amount -> {
            payments.add(amount);
            return "pay_completed";
        });
        var invoiceService = new InvoiceService((recipient, subject, body) -> {
            emails.add(new SentEmail(recipient, subject, body));
            throw deliveryFailure;
        });
        var receipt = orderService.placeOrder(new OrderRequest("keyboard-pro", 2, 25_000));

        var exception = assertThrowsExactly(IllegalStateException.class, () ->
                invoiceService.createInvoice(new InvoiceRequest("ada@example.com", receipt.totalCents())));

        // Two services do not form a distributed transaction: email failure cannot undo the charge.
        assertAll(
                () -> assertSame(deliveryFailure, exception),
                () -> assertEquals(new OrderReceipt("pay_completed", 50_000), receipt),
                () -> assertEquals(List.of(50_000L), payments),
                () -> assertEquals(List.of(new SentEmail(
                        "ada@example.com", "Your invoice is ready", "Amount due: 50000 cents")), emails)
        );
    }

    @Test
    void invoicesTheExactTotalProducedByTheOrderService() {
        var payments = new ArrayList<Long>();
        var emails = new ArrayList<SentEmail>();
        var orderService = new OrderService(
                (sku, quantity) -> sku.equals("keyboard-pro") && quantity <= 2,
                amount -> {
                    payments.add(amount);
                    return "pay_123";
                });
        var invoiceService = new InvoiceService(
                (recipient, subject, body) -> emails.add(new SentEmail(recipient, subject, body)));

        var receipt = orderService.placeOrder(new OrderRequest("keyboard-pro", 2, 25_000));
        var invoice = invoiceService.createInvoice(new InvoiceRequest("ada@example.com", receipt.totalCents()));

        assertAll(
                () -> assertEquals(new OrderReceipt("pay_123", 50_000), receipt),
                () -> assertEquals(new Invoice("ada@example.com", 50_000, "CREATED"), invoice),
                () -> assertEquals(List.of(50_000L), payments),
                () -> assertEquals(List.of(new SentEmail(
                        "ada@example.com", "Your invoice is ready", "Amount due: 50000 cents")), emails)
        );
    }

    @Test
    void unavailableStockStopsTheWorkflowBeforePaymentAndInvoiceEmail() {
        var payments = new ArrayList<Long>();
        var emails = new ArrayList<SentEmail>();
        var orderService = new OrderService((sku, quantity) -> false, amount -> {
            payments.add(amount);
            return "pay_unexpected";
        });
        var invoiceService = new InvoiceService(
                (recipient, subject, body) -> emails.add(new SentEmail(recipient, subject, body)));

        var exception = assertThrowsExactly(IllegalStateException.class, () -> {
            var receipt = orderService.placeOrder(new OrderRequest("keyboard-pro", 3, 25_000));
            invoiceService.createInvoice(new InvoiceRequest("ada@example.com", receipt.totalCents()));
        });

        assertAll(
                () -> assertEquals("not enough stock", exception.getMessage()),
                () -> assertEquals(List.of(), payments),
                () -> assertEquals(List.of(), emails)
        );
    }

    private record SentEmail(String recipient, String subject, String body) { }
}
