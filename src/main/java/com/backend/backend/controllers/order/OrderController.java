package com.backend.backend.controllers.order;

import com.backend.backend.dtos.order.OrderRequest;
import com.backend.backend.dtos.order.OrderResponse;
import com.backend.backend.entities.order.Order;
import com.backend.backend.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "https://shopping-center-lime.vercel.app")
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderDto){
        OrderResponse orderResponse = orderService.createOrder(orderDto);
        return ResponseEntity.ok(orderResponse);
    }
}
