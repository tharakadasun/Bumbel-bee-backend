package com.backend.backend.dtos.order;

import com.backend.backend.entities.order.OrderItem;
import com.backend.backend.entities.payment.Payment;
import com.backend.backend.entities.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Integer id;
    private Date orderDate;
    private List<OrderItemRequest> orderItems;
    private Integer paymentMethodId;
}
