package com.joaoscioli.testing;

import java.util.Objects;

public class OrderService {
    private final InventoryGateway inventoryGateway;
    private final PaymentGateway paymentGateway;

    public OrderService(InventoryGateway inventoryGateway, PaymentGateway paymentGateway) {
        this.inventoryGateway = Objects.requireNonNull(inventoryGateway);
        this.paymentGateway = Objects.requireNonNull(paymentGateway);
    }

    public OrderReceipt placeOrder(OrderRequest request) {
        validate(request);
        long totalCents = Math.multiplyExact((long) request.quantity(), request.unitPriceCents());

        if (!inventoryGateway.hasEnoughStock(request.sku(), request.quantity())) {
            throw new IllegalStateException("not enough stock");
        }

        String paymentId = paymentGateway.charge(totalCents);
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalStateException("payment gateway returned a blank payment id");
        }

        return new OrderReceipt(paymentId, totalCents);
    }

    private void validate(OrderRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        if (request.sku() == null || request.sku().isBlank()) {
            throw new IllegalArgumentException("sku must not be blank");
        }

        if (request.quantity() <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }

        if (request.unitPriceCents() <= 0) {
            throw new IllegalArgumentException("unitPriceCents must be greater than zero");
        }
    }
}
