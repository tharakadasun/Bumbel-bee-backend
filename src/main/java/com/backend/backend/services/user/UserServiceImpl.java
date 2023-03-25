package com.backend.backend.services.user;

import com.backend.backend.dtos.UserResponse;
import com.backend.backend.entities.user.Role;
import com.backend.backend.entities.user.User;
import com.backend.backend.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findByRole(Role.USER);
        List<UserResponse> userList = users.stream().map(user->new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDob()
        )).toList();
        return userList;
    }
}
