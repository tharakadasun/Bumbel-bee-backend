package com.backend.backend.services.user;

import com.backend.backend.dtos.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    List<UserResponse> getAllUsers();
}
