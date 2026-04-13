package com.core.apis.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderCommand {

    @TargetAggregateIdentifier  // Identificador del Aggregate al que se enviará este comando.
    private String orderId;     // Identificador único de la orden (Ej: UUID aleatorio).
    private String itemType;    // Tipo de producto (Ej: "LAPTOP", "MOUSE", "KEYBOARD").
    private BigDecimal price;   // Precio del producto.
    private String currency;    // Moneda en la que se expresa el precio (Ej: "USD", "EUR", "BOB").
    private String orderStatus; // Estado inicial deseado (Ej: "CREADO", "PENDIENTE")
}
