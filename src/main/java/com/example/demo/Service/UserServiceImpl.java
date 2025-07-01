package com.example.demo.Service;

import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.entity.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    @Transactional
    public User createUser(User user ) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        // Ensure user has no roles set explicitly
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role employeeRole = Role.builder()
                    .name("")
                    .label("")
                    .category(RoleCategory.SSEMP)
                    .parameter("")
                    .rolStruct(RoleStruct.SEMP)
                    .model(RoleModel.EMPLOYEE)
                    .modelLabel("")
                    .delegable(false)
                    .delegated(false)
                    .user(user)
                    .build();

            user.setRoles(Set.of(employeeRole));
        } else {
            user.getRoles().forEach(role -> role.setUser(user));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {


        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findByNudoss(id);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setFirstname(user.getFirstname());
        existingUser.setLastname(user.getLastname());
        existingUser.setPhonenumber(user.getPhonenumber());
        existingUser.setEmail(user.getEmail());
        existingUser.setMatcle(user.getMatcle());
        existingUser.setSoccle(user.getSoccle());
        existingUser.setLanguage(user.getLanguage());

        // Include image if provided
        if (user.getPhoto() != null) {
            existingUser.setPhoto(user.getPhoto());
            existingUser.setHasPhoto(true);
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        // Remove associated roles first
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            user.getRoles().clear();
        }

        userRepository.delete(user);
    }

    @Transactional
    public void updateUserMatcleAndSoccle(Long userId, String matcle, String soccle) {
        User user = userRepository.findByNudoss(userId);
        user.setMatcle(matcle);
        user.setSoccle(soccle);
        user.setUserId(matcle,soccle);
        userRepository.save(user);
    }
}
