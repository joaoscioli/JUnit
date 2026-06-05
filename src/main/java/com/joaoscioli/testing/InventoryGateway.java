package com.joaoscioli.testing;

public interface InventoryGateway {
    boolean hasEnoughStock(String sku, int quantity);
}
