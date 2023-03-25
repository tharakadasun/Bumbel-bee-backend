package com.backend.backend.controllers.user;

import com.backend.backend.dtos.UserResponse;
import com.backend.backend.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@CrossOrigin(origins = "https://shopping-center-lime.vercel.app")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    @GetMapping("/all")
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }
}
