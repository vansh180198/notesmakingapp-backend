package notesmaking.controller;

import jakarta.servlet.http.HttpServletRequest;
import notesmaking.emitter.SseEmitterService;
import notesmaking.model.User;
import notesmaking.model.UserDTO;
import notesmaking.service.UserService;
import notesmaking.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final SseEmitterService sseEmitterService;

    public UserController(UserService userService, SseEmitterService sseEmitterService) {
        this.userService = userService;
        this.sseEmitterService = sseEmitterService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user.getEmail(), user.getName(), user.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        boolean isAuthenticated = userService.authenticateUser(user.getEmail(), user.getPassword());
        if (isAuthenticated) {
            User loggedInUser = userService.getUserByEmail(user.getEmail());
            String token = JwtUtil.generateToken(loggedInUser.getId(), loggedInUser.getEmail());

            sseEmitterService.addEmitter(loggedInUser.getEmail());

            return ResponseEntity.ok(Map.of("message", "Login successful", "token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String email = JwtUtil.extractUserEmail(token);

        User user = userService.getUserByEmail(email);
        if (user != null) {
            UserDTO userDTO = new UserDTO(user.getId(), user.getEmail(), user.getName());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/stream")
    public SseEmitter streamOnlineUsers() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return sseEmitterService.addEmitter(userId);
    }

    @GetMapping("/online")
    public ResponseEntity<?> getOnlineUsers() {
        List<String> activeUsers = sseEmitterService.getActiveUsers();
        System.out.println(activeUsers);
        return ResponseEntity.ok(activeUsers);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String email = JwtUtil.extractUserEmail(token);

        sseEmitterService.removeEmitter(email);
        sseEmitterService.broadcastOnlineUsers();

        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }


}
