package com.joaoscioli.testing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
    @MethodSource("invalidRequests")
    void rejectsInvalidRequestsBeforeCallingExternalGateways(OrderRequest request) {
        OrderService service = new OrderService(inventoryGateway, paymentGateway);

        assertThrows(RuntimeException.class, () -> service.placeOrder(request));

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

    private static Stream<OrderRequest> invalidRequests() {
        return Stream.of(
                null,
                new OrderRequest("", 1, 25_000),
                new OrderRequest("keyboard-pro", 0, 25_000),
                new OrderRequest("keyboard-pro", 1, 0)
        );
    }
}
