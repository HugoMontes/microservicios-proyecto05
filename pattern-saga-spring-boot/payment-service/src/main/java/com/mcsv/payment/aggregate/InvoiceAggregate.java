package com.mcsv.payment.aggregate;

import com.core.apis.command.CreateInvoiceCommand;
import com.core.apis.event.InvoiceCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class InvoiceAggregate {

    // Identificador único del Aggregate (clave para Axon)
    @AggregateIdentifier
    private String paymentId;              // ID único de la transacción de pago
    private String orderId;                // Para saber a qué Orden pertenece este pago
    private InvoiceStatus invoiceStatus;   // Estado de la factura

    // Constructor vacío requerido por Axon
    public InvoiceAggregate() {
    }

    // CommandHandler que crea el Aggregate
    // Emite el evento InvoiceCreatedEvent.
    @CommandHandler
    public InvoiceAggregate(CreateInvoiceCommand createInvoiceCommand) {
        // Se aplica un evento en lugar de modificar directamente el estado
        AggregateLifecycle.apply(
                new InvoiceCreatedEvent(
                        createInvoiceCommand.getPaymentId(),
                        createInvoiceCommand.getOrderId()
                )
        );
    }

    // EventSourcingHandler que actualiza el estado del Aggregate
    // Cambiar el estado interno del Agregado cuando la factura se crea.
    @EventSourcingHandler
    public void on(InvoiceCreatedEvent invoiceCreatedEvent){
        this.paymentId = invoiceCreatedEvent.getPaymentId();
        this.orderId = invoiceCreatedEvent.getOrderId();
        // Se define el estado como pagado
        this.invoiceStatus = InvoiceStatus.PAID;
    }
}
