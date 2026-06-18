package com.joaoscioli.testing;

public interface EmailGateway {
    void sendInvoice(String customerEmail, String subject, String body);
}
