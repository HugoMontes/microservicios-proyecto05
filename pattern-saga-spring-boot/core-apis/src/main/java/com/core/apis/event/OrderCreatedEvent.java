package com.core.apis.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderCreatedEvent {
    private String orderId;     // ID único de la orden que acaba de nacer.
    private String itemType;    // Tipo de producto ("LAPTOP", "MOUSE").
    private BigDecimal price;   // Precio del producto.
    private String currency;    // Moneda ("USD", "EUR", "BOB").
    private String orderStatus; // Estado final de la orden al ser creada.
}
