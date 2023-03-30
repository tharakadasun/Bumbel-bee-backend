package com.backend.backend.services.order;

import com.backend.backend.dtos.order.OrderRequest;
import com.backend.backend.dtos.order.OrderResponse;
import com.backend.backend.entities.order.Order;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderDto);

    OrderResponse updateOrder(OrderRequest orderDto, Integer id);
}
