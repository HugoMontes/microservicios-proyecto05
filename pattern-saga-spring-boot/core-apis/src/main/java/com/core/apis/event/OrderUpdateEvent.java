package com.core.apis.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderUpdateEvent {
    private String orderId;     // ID de la orden que cambió
    private String orderStatus; // El nuevo estado de la orden
}
