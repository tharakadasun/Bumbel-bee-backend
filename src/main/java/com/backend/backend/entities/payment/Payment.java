package com.backend.backend.entities.payment;

import com.backend.backend.entities.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private String paymentMethodMeta;
    private Double totalAmount;
    private Double processingFee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private PaymentState paymentState;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "method_id", referencedColumnName = "id")
    private PaymentMethod method;
    public Payment(Double totalAmount, PaymentState paymentState, PaymentMethod method) {
        this.totalAmount = totalAmount;
        this.paymentState = paymentState;
        this.method = method;
    }
}
