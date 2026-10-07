package com.joaoscioli.testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscountCalculatorTest {
    private final DiscountCalculator calculator = new DiscountCalculator();

    @ParameterizedTest(name = "{0}: subtotal {1} yields {2} cents")
    @CsvSource({
            "STANDARD, 99999, 99999", "STANDARD, 100000, 95000", "STANDARD, 100001, 95001",
            "PREMIUM, 99999, 90000", "PREMIUM, 100000, 85000", "PREMIUM, 100001, 85001",
            "ENTERPRISE, 99999, 80000", "ENTERPRISE, 100000, 75000", "ENTERPRISE, 100001, 75001",
            "PREMIUM, 1, 1", "PREMIUM, 9, 9", "PREMIUM, 10, 9",
            "ENTERPRISE, 4, 4", "ENTERPRISE, 5, 4"
    })
    void appliesInclusiveBonusThresholdAndRoundsDiscountDownToWholeCents(
            CustomerTier tier, long subtotal, long expected) {
        assertEquals(expected, calculator.applyDiscount(subtotal, tier));
    }

    @Nested
    @DisplayName("tier discounts")
    class TierDiscounts {
        @ParameterizedTest(name = "{0} customer pays {2} cents from {1} cents")
        @CsvSource({
                "STANDARD, 10000, 10000",
                "PREMIUM, 10000, 9000",
                "ENTERPRISE, 10000, 8000"
        })
        void appliesBaseDiscountByCustomerTier(CustomerTier tier, long subtotalCents, long expectedCents) {
            long finalAmount = calculator.applyDiscount(subtotalCents, tier);

            assertEquals(expectedCents, finalAmount);
        }
    }

    @Nested
    @DisplayName("large order discounts")
    class LargeOrderDiscounts {
        @Test
        void addsBonusDiscountForLargeOrders() {
            long finalAmount = calculator.applyDiscount(100_000, CustomerTier.PREMIUM);

            assertEquals(85_000, finalAmount);
        }

        @Test
        void calculatesLargeEnterpriseOrderWithoutOverflow() {
            long finalAmount = calculator.applyDiscount(Long.MAX_VALUE, CustomerTier.ENTERPRISE);

            assertEquals(6_917_529_027_641_081_856L, finalAmount);
        }
    }

    @Nested
    @DisplayName("invalid input")
    class InvalidInput {
        @ParameterizedTest
        @ValueSource(longs = {0, -1, -500})
        void rejectsNonPositiveSubtotals(long subtotalCents) {
            var exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.applyDiscount(subtotalCents, CustomerTier.STANDARD)
            );

            assertEquals("subtotalCents must be greater than zero", exception.getMessage());
        }

        @Test
        void rejectsMissingCustomerTier() {
            assertThrows(
                    NullPointerException.class,
                    () -> calculator.applyDiscount(10000, null)
            );
        }
    }
}
