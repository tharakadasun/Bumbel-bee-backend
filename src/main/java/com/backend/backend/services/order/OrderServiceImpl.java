package com.backend.backend.services.order;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.backend.backend.dtos.order.OrderItemRequest;
import com.backend.backend.dtos.order.OrderRequest;
import com.backend.backend.dtos.order.OrderResponse;
import com.backend.backend.entities.loan.Loan;
import com.backend.backend.entities.order.Order;
import com.backend.backend.entities.order.OrderItem;
import com.backend.backend.entities.order.OrderStatus;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    @Value("${bucketName}")
    private String bucketName;

    @Value("${accessKey}")
    private String accessKey;

    @Value("${secret}")
    private String secret;
    @Value("${region}")
    private String region;

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
//            String imageName = product.getImageName();
            productPriceValue += product.getPerUnitPrice();
//            product.setImageName(generateImageURL(imageName));
            OrderItem orderItem = new OrderItem(product, orderItemDto.getQuantity());
            orderItems.add(orderItem);
        }
        Loan loan = currentUser.getLoan();
        if(loan.getLoanBalance()>productPriceValue){
            orderResponse.setIsLoanActive(true);
        }else{
            orderResponse.setIsLoanActive(false);
        }

        Order order = new Order();
        order.setOrderItems(orderItems);
        order.setStatus(OrderStatus.PENDING);
        order.setUser(currentUser);

        orderRepository.save(order);

        orderResponse.setOrderItems(orderItems);
        orderResponse.setIsOrderSuccess(true);
        orderResponse.setTotalAmount(productPriceValue);
        orderResponse.setId(order.getId());

        return orderResponse;
    }

    @Override
    public OrderResponse updateOrder(OrderRequest orderDto, Integer id) {
        Order order = orderRepository.findById(id).get();
        OrderResponse orderResponse = new OrderResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserName).get();

        Payment payment = new Payment();

        PaymentMethod paymentMethod = paymentMethodRepository.findById(orderDto.getPaymentMethodId()).get();

        List<OrderItem> orderItems = new ArrayList<>();
        double productPriceValue = 0.0;

        for (OrderItem order_item : order.getOrderItems()) {
            Product product = productRepository.findById(order_item.getProduct().getId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
//            String imageName = product.getImageName();
            productPriceValue += product.getPerUnitPrice();
//            product.setImageName(generateImageURL(imageName));
            OrderItem orderItem = new OrderItem(product, order_item.getQuantity());
            orderItems.add(orderItem);
        }

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

        order.setOrderItems(orderItems);
        order.setPayment(payment);
        order.setUser(currentUser);
        orderRepository.save(order);
        return null;
    }

    public String generateImageURL(String imageName){
        if(!imageName.equals(null)){
            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secret);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();
            Date expiration = new Date(System.currentTimeMillis() + 3600000);
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, imageName)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(expiration);
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        }else{
            return null;
        }
    }
}
