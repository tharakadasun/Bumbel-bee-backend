package com.backend.backend.services.auth;

import com.backend.backend.config.JwtService;
import com.backend.backend.controllers.auth.AuthenticationRequest;
import com.backend.backend.controllers.auth.AuthenticationResponse;
import com.backend.backend.controllers.auth.RegisterRequest;
import com.backend.backend.entities.loan.Loan;
import com.backend.backend.entities.user.Role;
import com.backend.backend.entities.user.User;
import com.backend.backend.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponse Register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        Loan loan = new Loan();
        LocalDate today = LocalDate.now();
        LocalDate dob = LocalDate.of(
                request.getDob().getYear(),
                request.getDob().getMonth(),
                request.getDob().getDay()
        );
        int age = Period.between(dob, today).getYears();
        if(request.getRole().toString().equals("user")){
            user.setRole(Role.USER);
            if(age>18){
                loan.setAmount(15000.0);
                loan.setLoanBalance(15000.0);
                loan.setUsedAmount(0.0);
                user.setLoan(loan);
            }else{
                loan = null;
            }
        }else{
            user.setRole(Role.ADMIN);
        }
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .build();
    }

    @Override
    public AuthenticationResponse Authenticate(AuthenticationRequest request) {
        Boolean status = false;
        String statusCode = "";
        String errorMsg = "";
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            status = true;
            statusCode = "200";
        } catch (AuthenticationException e) {
            status = false;
            statusCode = "403";
            errorMsg = "Invalid email or Password!";
            throw new BadCredentialsException("Invalid email or password", e);
        }
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .errorMsg(errorMsg)
                .status(status)
                .statusCode(statusCode)
                .build();
    }
}
