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
    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.NullAndEmptySource
    @org.junit.jupiter.params.provider.ValueSource(strings = {" ", "\t\n"})
    void missingPaymentConfirmationStopsInvoiceDeliveryWithoutRetryingAnUncertainCharge(String paymentId) {
        var calls = new ArrayList<String>();
        var orders = new OrderService((sku, quantity) -> {
            calls.add("stock:" + sku + ":" + quantity);
            return true;
        }, amount -> {
            calls.add("charge:" + amount);
            return paymentId;
        });
        var invoices = new InvoiceService((recipient, subject, body) -> calls.add("email"));

        var failure = assertThrowsExactly(IllegalStateException.class, () -> {
            var receipt = orders.placeOrder(new OrderRequest("keyboard-pro", 2, 25_000));
            invoices.createInvoice(new InvoiceRequest("ada@example.com", receipt.totalCents()));
        });

        assertEquals("payment gateway returned a blank payment id", failure.getMessage());
        assertEquals(List.of("stock:keyboard-pro:2", "charge:50000"), calls);
    }

    @Test
    void paymentFailureStopsInvoiceDeliveryAndDoesNotRetryAnUncertainCharge() {
        var calls = new ArrayList<String>();
        var failure = new IllegalStateException("payment outcome unknown");
        var orders = new OrderService((sku, quantity) -> {
            calls.add("stock:" + sku + ":" + quantity);
            return true;
        }, amount -> {
            calls.add("charge:" + amount);
            throw failure;
        });
        var invoices = new InvoiceService((recipient, subject, body) -> calls.add("email"));

        var actual = assertThrowsExactly(IllegalStateException.class, () -> {
            var receipt = orders.placeOrder(new OrderRequest("keyboard-pro", 2, 25_000));
            invoices.createInvoice(new InvoiceRequest("ada@example.com", receipt.totalCents()));
        });

        assertSame(failure, actual);
        assertEquals(List.of("stock:keyboard-pro:2", "charge:50000"), calls);
    }

    @Test
    void retryingInvoiceWithTheExistingReceiptDoesNotChargeTheOrderAgain() {
        var payments = new ArrayList<Long>();
        var emails = new ArrayList<SentEmail>();
        var failure = new IllegalStateException("email transport unavailable");
        var orders = new OrderService((sku, quantity) -> true, amount -> {
            payments.add(amount);
            return "pay_completed";
        });
        var invoices = new InvoiceService((recipient, subject, body) -> {
            emails.add(new SentEmail(recipient, subject, body));
            if (emails.size() == 1) {
                throw failure;
            }
        });
        var receipt = orders.placeOrder(new OrderRequest("keyboard-pro", 2, 25_000));
        var request = new InvoiceRequest("ada@example.com", receipt.totalCents());

        assertSame(failure, assertThrowsExactly(IllegalStateException.class,
                () -> invoices.createInvoice(request)));
        assertEquals(1, emails.size());

        // Retry only the failed boundary using the retained receipt, not placeOrder again.
        var invoice = invoices.createInvoice(request);

        var expectedEmail = new SentEmail("ada@example.com", "Your invoice is ready", "Amount due: 50000 cents");
        assertAll(
                () -> assertEquals(new Invoice("ada@example.com", 50_000, "CREATED"), invoice),
                () -> assertEquals(List.of(50_000L), payments),
                () -> assertEquals(List.of(expectedEmail, expectedEmail), emails)
        );
    }

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
