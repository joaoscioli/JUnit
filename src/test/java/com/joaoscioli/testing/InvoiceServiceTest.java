package com.joaoscioli.testing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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

    @ParameterizedTest
    @MethodSource("invalidInvoices")
    void rejectsInvalidInvoiceBeforeSendingEmail(InvoiceRequest request, String message) {
        InvoiceService service = new InvoiceService(emailGateway);

        var exception = assertThrowsExactly(IllegalArgumentException.class,
                () -> service.createInvoice(request));
        assertEquals(message, exception.getMessage());

        verifyNoInteractions(emailGateway);
    }

    @Test
    void propagatesEmailFailureWithoutReturningSuccessOrSendingTwice() {
        var service = new InvoiceService(emailGateway);
        var request = new InvoiceRequest("customer@example.com", 12_900);
        var failure = new IllegalStateException("email provider unavailable");
        doThrow(failure).when(emailGateway).sendInvoice(
                "customer@example.com", "Your invoice is ready", "Amount due: 12900 cents");

        assertSame(failure, assertThrowsExactly(IllegalStateException.class,
                () -> service.createInvoice(request)));

        verify(emailGateway).sendInvoice(
                "customer@example.com", "Your invoice is ready", "Amount due: 12900 cents");
        verifyNoMoreInteractions(emailGateway);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, Integer.MAX_VALUE})
    void preservesPositiveAmountBoundariesInTheInvoiceAndEmail(int amountCents) {
        var service = new InvoiceService(emailGateway);

        var invoice = service.createInvoice(new InvoiceRequest("customer@example.com", amountCents));

        assertEquals(new Invoice("customer@example.com", amountCents, "CREATED"), invoice);
        verify(emailGateway).sendInvoice("customer@example.com", "Your invoice is ready",
                "Amount due: " + amountCents + " cents");
        verifyNoMoreInteractions(emailGateway);
    }

    private static Stream<Arguments> invalidInvoices() {
        return Stream.of(
                Arguments.of(null, "request must not be null"),
                Arguments.of(new InvoiceRequest(null, 100), "customerEmail must not be blank"),
                Arguments.of(new InvoiceRequest("", 100), "customerEmail must not be blank"),
                Arguments.of(new InvoiceRequest(" \t\n", 100), "customerEmail must not be blank"),
                Arguments.of(new InvoiceRequest("customer@example.com", 0), "amountCents must be greater than zero"),
                Arguments.of(new InvoiceRequest("customer@example.com", -1), "amountCents must be greater than zero")
        );
    }
}
