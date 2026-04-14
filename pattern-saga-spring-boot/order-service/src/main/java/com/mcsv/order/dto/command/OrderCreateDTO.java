package com.mcsv.order.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDTO {
    private String itemType;    // Tipo de producto (Ej. LAPTOP, MOUSE, TECLADO)
    private BigDecimal price;   // Precio del producto
    private String currency;    // Tipo de moneda (Ej. USD, EUR, BOB)
}

