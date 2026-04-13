package com.core.apis.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderShippedEvent {
    private String shippingId;       // ID del envío
    private String orderId;          // ID de la orden que está siendo enviada
    private String paymentId;        // ID del pago que financió esta orden
}
