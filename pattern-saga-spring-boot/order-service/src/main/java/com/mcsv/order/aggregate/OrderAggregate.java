package com.mcsv.order.aggregate;

import com.core.apis.command.CreateOrderCommand;
import com.core.apis.command.UpdateOrderStatusCommand;
import com.core.apis.event.OrderCreatedEvent;
import com.core.apis.event.OrderUpdateEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate                          // Axon escanea esta clase y la registra como un Agregado manejado por Spring
public class OrderAggregate {

    @AggregateIdentifier             // Le dice a Axon: "Este campo es la LLAVE PRIMARIA del Agregado".
    private String orderId;          // Es el mismo valor que usa @TargetAggregateIdentifier en los Comandos.
    private ItemType itemType;       // Tipo de producto (enum: LAPTOP, MOUSE, etc.)
    private BigDecimal price;        // Precio del producto
    private String currency;         // Moneda (USD, EUR, BOB)
    private OrderStatus orderStatus; // Estado de la orden (enum: CREATED, APPROVED, SHIPPED, etc.)

    // Constructor vacio obligatorio para axon
    public OrderAggregate() {
    }

    // MANEJADOR DE COMANDO:
    // Este metodo se ejecuta cuando alguien envía un 'CreateOrderCommand' al CommandBus.
    // Si esta correcto, EMITE un 'OrderCreatedEvent' usando AggregateLifecycle.apply().
    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        // Aquí irían validaciones de negocio antes de emitir el evento.
        // Emite el Evento. Esto disparará el @EventSourcingHandler 'on(OrderCreatedEvent)'
        AggregateLifecycle.apply(new OrderCreatedEvent(
                        createOrderCommand.getOrderId(),
                        createOrderCommand.getItemType(),
                        createOrderCommand.getPrice(),
                        createOrderCommand.getCurrency(),
                        createOrderCommand.getOrderStatus()
                )
        );
    }

    // MANEJADOR DE COMANDO:
    // Se ejecuta cuando la SAGA (u otro servicio) envía un 'UpdateOrderStatusCommand'.
    // Ejemplo: PaymentService confirma pago -> Envía UpdateOrderStatusCommand con status = "APPROVED".
    @CommandHandler
    public void on(UpdateOrderStatusCommand updateOrderStatusCommand) {
        // Emite el evento de actualización.
        AggregateLifecycle.apply(new OrderUpdateEvent(
                updateOrderStatusCommand.getOrderId(),
                updateOrderStatusCommand.getOrderStatus())
        );
    }

    // MANEJADOR DE EVENTO SOURCING:
    // MODIFICAR EL ESTADO INTERNO del Agregado cuando ocurre un 'OrderCreatedEvent'.
    // Se ejecuta JUSTO DESPUÉS de que el @CommandHandler emita el evento (durante la ejecución actual).
    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        // Solo debe asignar valores a los campos 'private' del Agregado.
        this.orderId = orderCreatedEvent.getOrderId();
        this.itemType = ItemType.valueOf(orderCreatedEvent.getItemType());
        this.price = orderCreatedEvent.getPrice();
        this.currency = orderCreatedEvent.getCurrency();
        this.orderStatus = OrderStatus.valueOf(orderCreatedEvent.getOrderStatus());
    }

    // MANEJADOR DE EVENTO SOURCING:
    // Actualiza el estado de la orden cuando se emite un evento de actualización.
    @EventSourcingHandler
    public void on(OrderUpdateEvent orderUpdateEvent) {
        // Solo se actualiza el estado. Los demás campos (price, itemType) permanecen igual.
        this.orderId = orderUpdateEvent.getOrderId();
        this.orderStatus = OrderStatus.valueOf(orderUpdateEvent.getOrderStatus());
    }
}

