package com.core.apis.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateInvoiceCommand {
    @TargetAggregateIdentifier
    private String paymentId;   // ID unico de la transaccion de pago
    private String orderId;     // ID de la orden de compra
}
