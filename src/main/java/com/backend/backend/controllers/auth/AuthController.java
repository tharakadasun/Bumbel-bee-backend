package com.backend.backend.controllers.auth;

import com.backend.backend.services.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "https://shopping-center-lime.vercel.app")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> Register(
            @RequestBody RegisterRequest request
    ){
        System.out.println(request);
        return ResponseEntity.ok(authenticationService.Register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> Authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.Authenticate(request));
    }
}
