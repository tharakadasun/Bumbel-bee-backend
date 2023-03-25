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
public class OrderResponse {
    private Integer id;
    private List<OrderItem> orderItems;
    private Boolean isOrderSuccess;
    private String paymentMethod;
    private Boolean isLoanActive;
    private Double totalAmount;
}
