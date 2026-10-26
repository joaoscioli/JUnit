package com.joaoscioli.testing;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/** Real calculator and services, with recording fakes only at external boundaries. */
class DiscountedCheckoutIntegrationTest {
    @ParameterizedTest
    @CsvSource({
            "STANDARD, 9223372036854775807, 8762203435012037017",
            "PREMIUM, 9223372036854775807, 7839866231326559436",
            "ENTERPRISE, 9223372036854775807, 6917529027641081856",
            "STANDARD, 1, 1",
            "PREMIUM, 9, 9",
            "PREMIUM, 10, 9",
            "PREMIUM, 11, 10",
            "ENTERPRISE, 4, 4",
            "ENTERPRISE, 5, 4",
            "ENTERPRISE, 6, 5",
            "STANDARD, 99999, 99999",
            "STANDARD, 100000, 95000",
            "PREMIUM, 99999, 90000",
            "PREMIUM, 100000, 85000",
            "ENTERPRISE, 99999, 80000",
            "ENTERPRISE, 100000, 75000"
    })
    void discountBoundariesAndWholeCentRoundingReachBothPaymentAndInvoice(
            CustomerTier tier, long subtotal, long expectedTotal) {
        var charges = new ArrayList<Long>();
        var emails = new ArrayList<String>();
        var orders = new OrderService((sku, quantity) -> true, amount -> {
            charges.add(amount);
            return "pay-discounted";
        });
        var invoices = new InvoiceService((recipient, subject, body) -> emails.add(body));
        // Discount applies to the complete subtotal. Quantity one avoids per-unit rounding.
        var discountedTotal = new DiscountCalculator().applyDiscount(subtotal, tier);

        var receipt = orders.placeOrder(new OrderRequest("checkout-total", 1, discountedTotal));
        var invoice = invoices.createInvoice(new InvoiceRequest("ada@example.com", receipt.totalCents()));

        assertAll(
                () -> assertEquals(new OrderReceipt("pay-discounted", expectedTotal), receipt),
                () -> assertEquals(List.of(expectedTotal), charges),
                () -> assertEquals(new Invoice("ada@example.com", expectedTotal, "CREATED"), invoice),
                () -> assertEquals(List.of("Amount due: " + expectedTotal + " cents"), emails)
        );
    }
}
