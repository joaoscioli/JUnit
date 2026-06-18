package com.joaoscioli.testing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {
    @Mock
    private EmailGateway emailGateway;

    @Test
    void sendsInvoiceEmailWithGeneratedContent() {
        InvoiceService service = new InvoiceService(emailGateway);
        InvoiceRequest request = new InvoiceRequest("customer@example.com", 12_900);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);

        Invoice invoice = service.createInvoice(request);

        verify(emailGateway).sendInvoice(
                eq("customer@example.com"),
                subjectCaptor.capture(),
                bodyCaptor.capture()
        );

        assertAll(
                () -> assertEquals("customer@example.com", invoice.customerEmail()),
                () -> assertEquals(12_900, invoice.amountCents()),
                () -> assertEquals("CREATED", invoice.status()),
                () -> assertEquals("Your invoice is ready", subjectCaptor.getValue()),
                () -> assertEquals("Amount due: 12900 cents", bodyCaptor.getValue())
        );
    }

    @Test
    void rejectsInvalidInvoiceBeforeSendingEmail() {
        InvoiceService service = new InvoiceService(emailGateway);

        assertThrows(IllegalArgumentException.class,
                () -> service.createInvoice(new InvoiceRequest("customer@example.com", 0)));

        verifyNoInteractions(emailGateway);
    }
}
