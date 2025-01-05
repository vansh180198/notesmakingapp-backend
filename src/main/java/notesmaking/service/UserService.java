package notesmaking.service;

import notesmaking.exception.UserAlreadyExistsException;
import notesmaking.model.User;
import notesmaking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Initialize PasswordEncoder
    }

    public User registerUser(String email, String name, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists.");
        }

        String userId = UUID.randomUUID().toString();
        String hashedPassword = passwordEncoder.encode(password); // Hash the password
        User user = new User(userId, email, name, hashedPassword);
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " does not exist"));
    }

    public boolean authenticateUser(String email, String password) {
        User user = getUserByEmail(email);
        return passwordEncoder.matches(password, user.getPassword()); // Compare hashed passwords
    }

    public void saveRefreshToken(String email, String refreshToken) {
        User user = getUserByEmail(email);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    public boolean validateRefreshToken(String email, String token) {
        User user = getUserByEmail(email);
        return user.getRefreshToken() != null && user.getRefreshToken().equals(token);
    }
}
