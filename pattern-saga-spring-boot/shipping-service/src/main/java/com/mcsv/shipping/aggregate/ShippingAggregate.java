package com.mcsv.shipping.aggregate;

import com.core.apis.command.CreateShippingCommand;
import com.core.apis.event.OrderShippedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class ShippingAggregate {

    // Identificador único del Aggregate
    @AggregateIdentifier
    private String shippingId;
    private String orderId;       // Relación con la orden
    private String paymentId;     // Relación con el pago/factura

    // Constructor vacío requerido por Axon
    public ShippingAggregate() {
    }

    // CommandHandler que crea el envío
    // Se ejecuta cuando la SAGA (OrderManagementSaga) envía este comando
    @CommandHandler
    public ShippingAggregate(CreateShippingCommand createShippingCommand) {
        // Se aplica el evento en lugar de cambiar estado directamente
        AggregateLifecycle.apply(
                new OrderShippedEvent(
                        createShippingCommand.getShippingId(),
                        createShippingCommand.getOrderId(),
                        createShippingCommand.getPaymentId()
                )
        );
    }

    // EventSourcingHandler que actualiza el estado del Aggregate
    // Actualizar el estado interno del Agregado cuando se confirma el envío.
    @EventSourcingHandler
    public void on(OrderShippedEvent orderShippedEvent) {
        this.shippingId = orderShippedEvent.getShippingId();
        this.orderId = orderShippedEvent.getOrderId();
        this.paymentId = orderShippedEvent.getPaymentId();
    }
}
