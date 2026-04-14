package com.mcsv.order.service.command;

import com.mcsv.order.dto.command.OrderCreateDTO;

import java.util.concurrent.CompletableFuture;

public interface OrderCommandService {
    public CompletableFuture<String> createOrder(OrderCreateDTO orderCreateDTO);
}
