package com.joaoscioli.testing;

import java.util.Objects;

public class InvoiceService {
    private final EmailGateway emailGateway;

    public InvoiceService(EmailGateway emailGateway) {
        this.emailGateway = Objects.requireNonNull(emailGateway, "emailGateway must not be null");
    }

    public Invoice createInvoice(InvoiceRequest request) {
        validate(request);

        var invoice = new Invoice(request.customerEmail(), request.amountCents(), "CREATED");
        emailGateway.sendInvoice(
                request.customerEmail(),
                "Your invoice is ready",
                "Amount due: %d cents".formatted(request.amountCents())
        );

        return invoice;
    }

    private void validate(InvoiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }

        if (request.customerEmail() == null || request.customerEmail().isBlank()) {
            throw new IllegalArgumentException("customerEmail must not be blank");
        }

        if (request.amountCents() <= 0) {
            throw new IllegalArgumentException("amountCents must be greater than zero");
        }
    }
}
