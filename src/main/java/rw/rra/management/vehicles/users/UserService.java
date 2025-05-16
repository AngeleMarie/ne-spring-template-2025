package rw.rra.management.vehicles.users;

import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import rw.rra.management.vehicles.auth.dtos.RegisterRequestDto;
import rw.rra.management.vehicles.commons.exceptions.BadRequestException;
import rw.rra.management.vehicles.users.dtos.UserResponseDto;
import rw.rra.management.vehicles.users.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rw.rra.management.vehicles.vehicles.VehicleRepository;

import java.util.Collection;
import java.util.UUID;

@Service
@AllArgsConstructor

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:amrieangele@gmail.com}")
    private String adminEmail;

    @Value("${admin.password:Angele@2025}")
    private String adminPassword;

    public UserResponseDto createUser(RegisterRequestDto user) {
        if(userRepository.existsByEmailOrPhoneNumberOrNationalId(user.email(), user.phoneNumber(), user.nationalId()))
            throw new BadRequestException("User with this email or nationalId or phone number already exists.");

        var newUser = userMapper.toEntity(user);
        newUser.setPassword(passwordEncoder.encode(user.password()));
        newUser.setRole(Role.ROLE_STANDARD);
        newUser.setEnabled(false);
        log.info("user is here, {}", newUser);
        userRepository.save(newUser);
        return userMapper.toResponseDto(newUser);
    }

    public void createAdminUserIfNotExists() {
        // Check if the admin user already exists
        if (!userRepository.findByEmail(adminEmail).isPresent()) {
            User adminUser = new User();
            adminUser.setId(UUID.randomUUID());
            adminUser.setFirstName("Angele");
            adminUser.setLastName("Marie");
            adminUser.setEnabled(true);
            adminUser.setStatus(Status.ACTIVE);
            adminUser.setPhoneNumber("0798978831");
            adminUser.setNationalId("1200670162551080");
            adminUser.setEmail(adminEmail);
            // Use the password from environment variables, encoded
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRole(Role.ROLE_ADMIN);

            userRepository.save(adminUser);
            log.info("Admin user 'Angele' created.");
        } else {
            log.info("Admin user 'Angele' already exists.");
        }
    }

    public void changeUserPassword(String userEmail, String newPassword) {
        var user = findByEmail(userEmail);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void activateUserAccount(String userEmail) {
        var user = findByEmail(userEmail);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void updateUserStatus(String email, Status newStatus) {
        var user = findByEmail(email);
        user.setStatus(newStatus);
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User with that email not found."));
    }

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new BadRequestException("User is not authenticated.");
        }

        String userId = auth.getName(); // This is the user ID
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new BadRequestException("User with that ID not found."));
    }

    public UserResponseDto getCurrentLoggedInUser() {
        return userMapper.toResponseDto(getAuthenticatedUser());
    }



}
