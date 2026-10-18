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

    @ParameterizedTest(name = "{0}: adding 100 cents in bonus band {1} adds {2} payable cents")
    @CsvSource({"STANDARD, false, 100", "PREMIUM, false, 90", "ENTERPRISE, false, 80",
            "STANDARD, true, 95", "PREMIUM, true, 85", "ENTERPRISE, true, 75"})
    void wholeCurrencyIncrementPreservesCentRemaindersWithinEachPricingBand(
            CustomerTier tier, boolean bonusBand, long expectedIncrement) {
        var random = new java.util.Random(20261018L);
        for (int sample = 0; sample < 500; sample++) {
            long subtotal = bonusBand
                    ? random.nextLong(100_000, Long.MAX_VALUE - 100)
                    : random.nextLong(1, 99_900);
            long original = calculator.applyDiscount(subtotal, tier);
            long incremented = calculator.applyDiscount(subtotal + 100, tier);

            // Metamorphic check: compare related inputs without duplicating the rounding formula.
            assertEquals(expectedIncrement, incremented - original,
                    () -> "Unexpected payable increment for " + tier + " at subtotal " + subtotal);
        }
    }

    @Test
    void seededSubtotalsKeepAmountsBoundedAndTierBenefitsOrdered() {
        var random = new java.util.Random(20261013L);
        for (int sample = 0; sample < 1_000; sample++) {
            long subtotal = sample % 2 == 0
                    ? random.nextLong(1, 200_001)
                    : random.nextLong(1, Long.MAX_VALUE);
            long standard = calculator.applyDiscount(subtotal, CustomerTier.STANDARD);
            long premium = calculator.applyDiscount(subtotal, CustomerTier.PREMIUM);
            long enterprise = calculator.applyDiscount(subtotal, CustomerTier.ENTERPRISE);

            // Business invariants independent of the percentage calculation formula.
            org.junit.jupiter.api.Assertions.assertTrue(
                    0 < enterprise && enterprise <= premium && premium <= standard && standard <= subtotal,
                    () -> "Tier ordering or monetary bounds violated for subtotal " + subtotal);
        }
    }

    @ParameterizedTest(name = "{0}: maximum long subtotal yields {1} cents")
    @CsvSource({
            "STANDARD, 8762203435012037017",
            "PREMIUM, 7839866231326559436",
            "ENTERPRISE, 6917529027641081856"
    })
    void allTiersPreserveExactCentsWhenPercentageMultiplicationExceedsLongRange(
            CustomerTier tier, long expected) {
        assertEquals(expected, calculator.applyDiscount(Long.MAX_VALUE, tier));
    }

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
