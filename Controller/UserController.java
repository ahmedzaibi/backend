package com.example.demo.Controller;

import com.example.demo.Repository.UserRepository;
import com.example.demo.Security.CustomUserDetailsService;
import com.example.demo.Security.JwtUtil;
import com.example.demo.Service.UserService;
import com.example.demo.dtos.AuthRequest;
import com.example.demo.dtos.AuthResponse;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
@Autowired
UserService userService;
@Autowired
    JwtUtil jwtUtil;
@Autowired
private UserRepository userRepository;

private final AuthenticationManager authenticationManager;
@Autowired
CustomUserDetailsService customUserDetailsService;
    public UserController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @PutMapping("/{id}/update-matcle-soccle")
    public ResponseEntity<String> updateMatcleAndSoccle(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String matcle = request.get("matcle");
        String soccle = request.get("soccle");

        if (matcle == null || soccle == null) {
            return ResponseEntity.badRequest().body("Both matcle and soccle must be provided.");
        }

        userService.updateUserMatcleAndSoccle(id, matcle, soccle);
        return ResponseEntity.ok("Matcle and Soccle updated successfully for user ID: " + id);
    }


    @PostMapping("/register")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    @PostMapping("/authentification")
    public ResponseEntity<AuthResponse> authentification(@RequestBody AuthRequest request) {
        System.out.println("Attempting authentication for user: " + request.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            System.out.println("Authentication successful for user: " + request.getUsername());
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .token(null)
                            .message("Invalid credentials")
                            .build());        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getUsername());
        String jwtToken = jwtUtil.generateToken(userRepository.findByUsername(userDetails.getUsername()));

        return ResponseEntity.ok(AuthResponse.builder()
                .token(jwtToken)
                .message("Authentication successful")
                .build());    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/updateUser/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/delete-user/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
