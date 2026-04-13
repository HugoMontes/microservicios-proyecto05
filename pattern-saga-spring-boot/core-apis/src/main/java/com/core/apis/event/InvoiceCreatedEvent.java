package com.core.apis.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceCreatedEvent {
    private String paymentId;   // ID de la factura/pago (Ej: "PAY-456")
    private String orderId;     // ID de la orden de compra que originó este pago
}
