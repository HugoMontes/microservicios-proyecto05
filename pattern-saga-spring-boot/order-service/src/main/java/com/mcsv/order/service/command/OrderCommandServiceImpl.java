package com.mcsv.order.service.command;

import com.core.apis.command.CreateOrderCommand;
import com.mcsv.order.aggregate.OrderStatus;
import com.mcsv.order.dto.command.OrderCreateDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderCommandServiceImpl implements OrderCommandService {

    @Autowired
    private CommandGateway commandGateway; // Nos permite enviar comandos para luego generar eventos

    @Override
    public CompletableFuture<String> createOrder(OrderCreateDTO orderCreateDTO) {
        // PASO 1: Generar el ID único de la Orden
        String orderId = UUID.randomUUID().toString();
        // UUID.randomUUID() genera algo como: "a1b2c3d4-e5f6-7890-abcd-ef1234567890"

        // PASO 2: Construir el Comando de Axon
        CreateOrderCommand command = new CreateOrderCommand(
                orderId,                                    // ID generado
                orderCreateDTO.getItemType(),               // Tipo de producto ("LAPTOP")
                orderCreateDTO.getPrice(),                  // Precio (99.99)
                orderCreateDTO.getCurrency(),               // Moneda ("USD")
                String.valueOf(OrderStatus.CREATED)         // Estado inicial SIEMPRE "CREATED"
        );

        // PASO 3: Enviar el comando a través del Gateway y devolver el Future
        return commandGateway.send(command);
        // NOTA: El metodo send() es ASÍNCRONO.
        // El hilo HTTP se libera inmediatamente y el comando se procesa en segundo plano.
        // El CompletableFuture devuelto se completará cuando el OrderAggregate termine de procesar.
    }
}
