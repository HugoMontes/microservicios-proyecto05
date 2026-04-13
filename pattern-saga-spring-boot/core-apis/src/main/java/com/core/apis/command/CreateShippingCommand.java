package com.core.apis.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateShippingCommand {

    // Le dice a Axon "Entrega este comando al 'ShippingAggregate' que tenga este shippingId".
    @TargetAggregateIdentifier
    private String shippingId;  // ID único de este envío (Ej: "SHIP-001")

    private String orderId;     // ID de la Orden de compra original
    private String paymentId;   // ID del Pago confirmado
}
