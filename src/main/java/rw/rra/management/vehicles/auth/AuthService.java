package rw.rra.management.vehicles.auth;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import rw.rra.management.vehicles.auth.dtos.LoginRequestDto;
import rw.rra.management.vehicles.auth.dtos.LoginResponse;
import rw.rra.management.vehicles.users.Status;
import rw.rra.management.vehicles.users.User;
import rw.rra.management.vehicles.users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public User getCurrentUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var userId = (UUID) authentication.getPrincipal();

        return userRepository.findById(userId).orElse(null);
    }

    public LoginResponse login(LoginRequestDto loginRequest, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        var user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getStatus().equals(Status.ACTIVE)) {
            throw new IllegalStateException("User account is not active. Please verify your account first.");
        }

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/api/v1/auth/refresh");
        cookie.setMaxAge(60 * 60 * 24 * 7); // 30 days
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new LoginResponse(
                accessToken.toString()
        );
    }
}
