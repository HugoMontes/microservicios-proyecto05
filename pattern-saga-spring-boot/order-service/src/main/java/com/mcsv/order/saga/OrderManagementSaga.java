package com.mcsv.order.saga;

import com.core.apis.command.CreateInvoiceCommand;
import com.core.apis.command.CreateShippingCommand;
import com.core.apis.command.UpdateOrderStatusCommand;
import com.core.apis.event.InvoiceCreatedEvent;
import com.core.apis.event.OrderCreatedEvent;
import com.core.apis.event.OrderShippedEvent;
import com.core.apis.event.OrderUpdateEvent;
import com.mcsv.order.aggregate.OrderStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
public class OrderManagementSaga {

    @Autowired
    private transient CommandGateway commandGateway; // Gateway para enviar comandos a otros Aggregates.

    // Punto de inicio de la Saga.
    // Se ejecuta cuando se recibe el evento OrderCreatedEvent.
    @StartSaga
    @SagaEventHandler(associationProperty = "orderId") // Vincula la Saga con ese identificador.
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        String paymentId = UUID.randomUUID().toString();
        System.out.println("SAGA iniciada");
        // Asociamos la Saga con el paymentId para poder manejar eventos relacionados al pago
        SagaLifecycle.associateWith("paymentId", paymentId);
        System.out.println("Order ID : " + orderCreatedEvent.getOrderId());
        // Enviamos el comando para crear la factura (Invoice)
        commandGateway.send(new CreateInvoiceCommand(paymentId, orderCreatedEvent.getOrderId()));
    }

    // Continúa la Saga cuando se crea la factura.
    // Se activa cuando llega InvoiceCreatedEvent.
    // Usa paymentId para correlacionar con la Saga.
    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(InvoiceCreatedEvent invoiceCreatedEvent) {
        String shippingId = UUID.randomUUID().toString();
        System.out.println("SAGA - Invoice creada");
        // Asociamos la SAGA con shipping
        SagaLifecycle.associateWith("shippingId", shippingId);
        // Enviamos el comando para crear el envío (Shipping)
        commandGateway.send(
                new CreateShippingCommand(
                        shippingId,
                        invoiceCreatedEvent.getOrderId(),
                        invoiceCreatedEvent.getPaymentId()
                )
        );
    }

    // Se ejecuta cuando el pedido ha sido enviado.
    // Actualiza el estado de la orden a SHIPPED.
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent orderShippedEvent) {
        System.out.println("SAGA - Envío completado. Actualizando Orden a SHIPPED.");
        commandGateway.send(
                new UpdateOrderStatusCommand(
                        orderShippedEvent.getOrderId(),
                        String.valueOf(OrderStatus.SHIPPED)
                )
        );
    }

    // Último paso de la Saga.
    // Se ejecuta cuando se actualiza la orden.
    // Recibe la confirmación de que la Orden fue actualizada a "SHIPPED".
    // Finaliza la Saga liberando recursos.
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderUpdateEvent orderUpdateEvent) {
        System.out.println("SAGA finalizada");
        SagaLifecycle.end();
    }
}
