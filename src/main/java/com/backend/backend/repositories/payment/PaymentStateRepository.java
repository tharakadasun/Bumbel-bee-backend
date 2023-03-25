package com.backend.backend.repositories.payment;

import com.backend.backend.entities.payment.PaymentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStateRepository extends JpaRepository<PaymentState,Integer> {
    PaymentState findByPaymentStatusMeta(String meta);
}
