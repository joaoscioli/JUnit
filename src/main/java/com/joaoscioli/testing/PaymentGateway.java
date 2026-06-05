package com.joaoscioli.testing;

public interface PaymentGateway {
    String charge(long amountCents);
}
