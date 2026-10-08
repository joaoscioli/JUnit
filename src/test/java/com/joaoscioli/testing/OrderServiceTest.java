package com.joaoscioli.testing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private InventoryGateway inventoryGateway;

    @Mock
    private PaymentGateway paymentGateway;

    @Test
    void chargesPaymentWhenInventoryIsAvailable() {
        OrderService service = new OrderService(inventoryGateway, paymentGateway);
        OrderRequest request = new OrderRequest("keyboard-pro", 2, 25_000);

        when(inventoryGateway.hasEnoughStock("keyboard-pro", 2)).thenReturn(true);
        when(paymentGateway.charge(50_000)).thenReturn("pay_123");

        OrderReceipt receipt = service.placeOrder(request);

        assertAll(
                () -> assertEquals("pay_123", receipt.paymentId()),
                () -> assertEquals(50_000, receipt.totalCents())
        );

        verify(inventoryGateway).hasEnoughStock("keyboard-pro", 2);
        verify(paymentGateway).charge(50_000);
    }

    @Test
    void doesNotChargePaymentWhenInventoryIsUnavailable() {
        OrderService service = new OrderService(inventoryGateway, paymentGateway);
        OrderRequest request = new OrderRequest("keyboard-pro", 3, 25_000);

        when(inventoryGateway.hasEnoughStock("keyboard-pro", 3)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> service.placeOrder(request));

        verify(inventoryGateway).hasEnoughStock("keyboard-pro", 3);
        verifyNoInteractions(paymentGateway);
    }

    @ParameterizedTest
    @MethodSource("positiveTotalBoundaries")
    void chargesExactPositiveTotalsInStockBeforePaymentOrder(int quantity, long unitPrice, long expectedTotal) {
        var service = new OrderService(inventoryGateway, paymentGateway);
        when(inventoryGateway.hasEnoughStock("boundary-sku", quantity)).thenReturn(true);
        when(paymentGateway.charge(expectedTotal)).thenReturn("pay_exact");

        var receipt = service.placeOrder(new OrderRequest("boundary-sku", quantity, unitPrice));

        assertEquals(new OrderReceipt("pay_exact", expectedTotal), receipt);
        var calls = inOrder(inventoryGateway, paymentGateway);
        calls.verify(inventoryGateway).hasEnoughStock("boundary-sku", quantity);
        calls.verify(paymentGateway).charge(expectedTotal);
        verifyNoMoreInteractions(inventoryGateway, paymentGateway);
    }

    private static Stream<Arguments> positiveTotalBoundaries() {
        return Stream.of(
                Arguments.of(1, 1L, 1L),
                Arguments.of(2, 1_500_000_000L, 3_000_000_000L),
                Arguments.of(2, Long.MAX_VALUE / 2, Long.MAX_VALUE - 1)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    void rejectsInvalidRequestsBeforeCallingExternalGateways(
            OrderRequest request, Class<? extends RuntimeException> exceptionType, String message) {
        OrderService service = new OrderService(inventoryGateway, paymentGateway);

        var exception = assertThrowsExactly(exceptionType, () -> service.placeOrder(request));
        assertEquals(message, exception.getMessage());

        verifyNoInteractions(inventoryGateway, paymentGateway);
    }

    @Test
    void rejectsOverflowingTotalBeforeCallingExternalGateways() {
        OrderService service = new OrderService(inventoryGateway, paymentGateway);
        OrderRequest request = new OrderRequest("keyboard-pro", 2, Long.MAX_VALUE / 2 + 1);

        assertThrows(ArithmeticException.class, () -> service.placeOrder(request));

        verifyNoInteractions(inventoryGateway, paymentGateway);
    }

    @Test
    void chargesLargestRepresentableTotalWithoutOverflow() {
        OrderService service = new OrderService(inventoryGateway, paymentGateway);
        OrderRequest request = new OrderRequest("keyboard-pro", 1, Long.MAX_VALUE);
        when(inventoryGateway.hasEnoughStock("keyboard-pro", 1)).thenReturn(true);
        when(paymentGateway.charge(Long.MAX_VALUE)).thenReturn("pay_boundary");

        OrderReceipt receipt = service.placeOrder(request);

        assertAll(
                () -> assertEquals("pay_boundary", receipt.paymentId()),
                () -> assertEquals(Long.MAX_VALUE, receipt.totalCents())
        );
        verify(paymentGateway).charge(Long.MAX_VALUE);
    }

    @Test
    void propagatesInventoryFailureWithoutAttemptingPayment() {
        var service = new OrderService(inventoryGateway, paymentGateway);
        var request = new OrderRequest("keyboard-pro", 2, 25_000);
        var failure = new IllegalStateException("inventory unavailable");
        when(inventoryGateway.hasEnoughStock("keyboard-pro", 2)).thenThrow(failure);

        assertSame(failure, assertThrowsExactly(IllegalStateException.class, () -> service.placeOrder(request)));

        verify(inventoryGateway).hasEnoughStock("keyboard-pro", 2);
        verifyNoMoreInteractions(inventoryGateway);
        verifyNoInteractions(paymentGateway);
    }

    @Test
    void propagatesPaymentFailureWithoutRetryingTheCharge() {
        var service = new OrderService(inventoryGateway, paymentGateway);
        var request = new OrderRequest("keyboard-pro", 2, 25_000);
        var failure = new IllegalStateException("payment unavailable");
        when(inventoryGateway.hasEnoughStock("keyboard-pro", 2)).thenReturn(true);
        when(paymentGateway.charge(50_000)).thenThrow(failure);

        assertSame(failure, assertThrowsExactly(IllegalStateException.class, () -> service.placeOrder(request)));

        var calls = inOrder(inventoryGateway, paymentGateway);
        calls.verify(inventoryGateway).hasEnoughStock("keyboard-pro", 2);
        calls.verify(paymentGateway).charge(50_000);
        verifyNoMoreInteractions(inventoryGateway, paymentGateway);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n"})
    void rejectsBlankPaymentConfirmationWithoutRetryingTheCharge(String paymentId) {
        var service = new OrderService(inventoryGateway, paymentGateway);
        var request = new OrderRequest("keyboard-pro", 2, 25_000);
        when(inventoryGateway.hasEnoughStock("keyboard-pro", 2)).thenReturn(true);
        when(paymentGateway.charge(50_000)).thenReturn(paymentId);

        var exception = assertThrowsExactly(IllegalStateException.class, () -> service.placeOrder(request));
        assertEquals("payment gateway returned a blank payment id", exception.getMessage());
        var calls = inOrder(inventoryGateway, paymentGateway);
        calls.verify(inventoryGateway).hasEnoughStock("keyboard-pro", 2);
        calls.verify(paymentGateway).charge(50_000);
        verifyNoMoreInteractions(inventoryGateway, paymentGateway);
    }

    private static Stream<Arguments> invalidRequests() {
        return Stream.of(
                Arguments.of(null, NullPointerException.class, "request must not be null"),
                Arguments.of(new OrderRequest(null, 1, 25_000), IllegalArgumentException.class, "sku must not be blank"),
                Arguments.of(new OrderRequest("", 1, 25_000), IllegalArgumentException.class, "sku must not be blank"),
                Arguments.of(new OrderRequest(" \t", 1, 25_000), IllegalArgumentException.class, "sku must not be blank"),
                Arguments.of(new OrderRequest("keyboard-pro", 0, 25_000), IllegalArgumentException.class, "quantity must be greater than zero"),
                Arguments.of(new OrderRequest("keyboard-pro", -1, 25_000), IllegalArgumentException.class, "quantity must be greater than zero"),
                Arguments.of(new OrderRequest("keyboard-pro", 1, 0), IllegalArgumentException.class, "unitPriceCents must be greater than zero"),
                Arguments.of(new OrderRequest("keyboard-pro", 1, -1), IllegalArgumentException.class, "unitPriceCents must be greater than zero")
        );
    }
}
