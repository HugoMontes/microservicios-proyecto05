package com.core.apis.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOrderStatusCommand {
    @TargetAggregateIdentifier
    private String orderId;         // ID de la orden que queremos modificar
    private String orderStatus;     // El nuevo estado que tendrá la orden
}
