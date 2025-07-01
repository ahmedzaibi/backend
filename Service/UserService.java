package com.example.demo.Service;

import com.example.demo.dtos.AuthRequest;
import com.example.demo.dtos.AuthResponse;
import com.example.demo.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    User createUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, User user);
    void deleteUser(Long id);


    void updateUserMatcleAndSoccle(Long userId,String soccle, String matcle);
}