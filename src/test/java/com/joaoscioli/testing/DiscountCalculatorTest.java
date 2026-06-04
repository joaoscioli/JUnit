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
    }

    @Nested
    @DisplayName("invalid input")
    class InvalidInput {
        @ParameterizedTest
        @ValueSource(longs = {0, -1, -500})
        void rejectsNonPositiveSubtotals(long subtotalCents) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.applyDiscount(subtotalCents, CustomerTier.STANDARD)
            );
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
