package com.backend.backend.controllers.order;

import com.backend.backend.dtos.order.OrderRequest;
import com.backend.backend.dtos.order.OrderResponse;
import com.backend.backend.entities.order.Order;
import com.backend.backend.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderDto){
        System.out.println(orderDto);
        OrderResponse orderResponse = orderService.createOrder(orderDto);
        return ResponseEntity.ok(orderResponse);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@RequestBody OrderRequest orderDto, @PathVariable Integer id){
        System.out.println(id);
        OrderResponse orderResponse = orderService.updateOrder(orderDto,id);
        return ResponseEntity.ok(orderResponse);
    }
}
