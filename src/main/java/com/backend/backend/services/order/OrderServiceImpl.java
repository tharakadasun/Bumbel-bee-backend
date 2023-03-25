package com.backend.backend.services.order;

import com.backend.backend.dtos.order.OrderItemRequest;
import com.backend.backend.dtos.order.OrderRequest;
import com.backend.backend.dtos.order.OrderResponse;
import com.backend.backend.entities.loan.Loan;
import com.backend.backend.entities.order.Order;
import com.backend.backend.entities.order.OrderItem;
import com.backend.backend.entities.payment.Payment;
import com.backend.backend.entities.payment.PaymentMethod;
import com.backend.backend.entities.payment.PaymentState;
import com.backend.backend.entities.product.Product;
import com.backend.backend.entities.user.User;
import com.backend.backend.repositories.loan.LoanRepository;
import com.backend.backend.repositories.order.OrderRepository;
import com.backend.backend.repositories.payment.PaymentMethodRepository;
import com.backend.backend.repositories.payment.PaymentRepository;
import com.backend.backend.repositories.payment.PaymentStateRepository;
import com.backend.backend.repositories.product.ProductRepository;
import com.backend.backend.repositories.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final LoanRepository loanRepository;
    private final PaymentStateRepository paymentStateRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;


    @Override
    public OrderResponse createOrder(OrderRequest orderDto) {

        OrderResponse orderResponse = new OrderResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserName).get();

        List<OrderItem> orderItems = new ArrayList<>();
        double productPriceValue = 0.0;

        for (OrderItemRequest orderItemDto : orderDto.getOrderItems()) {
            Product product = productRepository.findById(orderItemDto.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
            productPriceValue += product.getPerUnitPrice();
            OrderItem orderItem = new OrderItem(product, orderItemDto.getQuantity());
            orderItems.add(orderItem);
        }
        Payment payment = new Payment();

        PaymentMethod paymentMethod = paymentMethodRepository.findById(orderDto.getPaymentMethodId()).get();

        if(paymentMethod.getPaymentMethodMeta().toString().equals("BUY_FIRST_PAY_LATER")){
            Loan loan = currentUser.getLoan();
            if(loan.getLoanBalance()>productPriceValue){
                double balance = loan.getLoanBalance()-productPriceValue;
                orderResponse.setIsLoanActive(true);
                loan.setLoanBalance(balance);
                loanRepository.save(loan);
            }else{
                orderResponse.setIsLoanActive(false);
            }
        }

        payment.setTotalAmount(productPriceValue);
        payment.setMethod(paymentMethod);
        PaymentState paymentState = paymentStateRepository.findByPaymentStatusMeta("PENDING");
        payment.setPaymentState(paymentState);
        paymentRepository.save(payment);

        Order order = new Order();
        order.setOrderItems(orderItems);
        order.setPayment(payment);
        order.setUser(currentUser);

        orderRepository.save(order);

        orderResponse.setOrderItems(orderItems);
        orderResponse.setIsOrderSuccess(true);
        orderResponse.setPaymentMethod(paymentMethod.getPaymentMethod());
        orderResponse.setId(order.getId());

        return orderResponse;
    }
}
