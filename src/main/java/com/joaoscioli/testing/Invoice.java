package com.joaoscioli.testing;

public record Invoice(String customerEmail, long amountCents, String status) {
}
