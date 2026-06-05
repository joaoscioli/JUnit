package com.joaoscioli.testing;

public record OrderRequest(String sku, int quantity, long unitPriceCents) {
}
