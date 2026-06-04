package com.joaoscioli.testing;

import java.util.Objects;

public class DiscountCalculator {
    private static final long LARGE_ORDER_THRESHOLD_CENTS = 100_000;
    private static final int LARGE_ORDER_BONUS_PERCENTAGE = 5;

    public long applyDiscount(long subtotalCents, CustomerTier tier) {
        if (subtotalCents <= 0) {
            throw new IllegalArgumentException("subtotalCents must be greater than zero");
        }

        Objects.requireNonNull(tier, "tier must not be null");

        int discountPercentage = baseDiscountFor(tier);

        if (subtotalCents >= LARGE_ORDER_THRESHOLD_CENTS) {
            discountPercentage += LARGE_ORDER_BONUS_PERCENTAGE;
        }

        return subtotalCents - (subtotalCents * discountPercentage / 100);
    }

    private int baseDiscountFor(CustomerTier tier) {
        return switch (tier) {
            case STANDARD -> 0;
            case PREMIUM -> 10;
            case ENTERPRISE -> 20;
        };
    }
}
