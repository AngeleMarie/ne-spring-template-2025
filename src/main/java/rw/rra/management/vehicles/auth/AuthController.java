package rw.rra.management.vehicles.auth;

import rw.rra.management.vehicles.auth.dtos.*;
import rw.rra.management.vehicles.commons.exceptions.BadRequestException;
import rw.rra.management.vehicles.email.EmailService;
import rw.rra.management.vehicles.users.Status;
import rw.rra.management.vehicles.users.UserService;
import rw.rra.management.vehicles.users.dtos.UserResponseDto;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rw.rra.management.vehicles.vehicles.dtos.VehicleResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;



    @PostMapping("/register")
    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody
                                                            RegisterRequestDto user, UriComponentsBuilder uriBuilder){
        var userResponse = userService.createUser(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userResponse.id()).toUri();
        // Use otp service to send otp to a registered user
        var otpToSend = otpService.generateOtp(userResponse.email(), OtpType.VERIFY_ACCOUNT);

        // Send email to the user with the OTP
        emailService.sendAccountVerificationEmail(userResponse.email(), userResponse.firstName(), otpToSend);
        return ResponseEntity.created(uri).body(userResponse);
    }

    @PatchMapping("/verify-account")
    @RateLimiter(name = "otp-rate-limiter")
    ResponseEntity<?> verifyAccount(@Valid @RequestBody VerifyAccountDto verifyAccountRequest){
        if (!otpService.verifyOtp(verifyAccountRequest.email(), verifyAccountRequest.otp(), OtpType.VERIFY_ACCOUNT))
            throw new BadRequestException("Invalid email or OTP");

        userService.activateUserAccount(verifyAccountRequest.email());

        var user = userService.findByEmail(verifyAccountRequest.email());
        userService.updateUserStatus(user.getEmail(), Status.ACTIVE);

        emailService.sendVerificationSuccessEmail(user.getEmail(), user.getFirstName());

        return ResponseEntity.ok("Account Activated successfully");
    }


    @PostMapping("/initiate-password-reset")
    ResponseEntity<?> initiatePasswordReset(@Valid @RequestBody InitiatePasswordResetDto initiateRequest){
        var otpToSend = otpService.generateOtp(initiateRequest.email(), OtpType.FORGOT_PASSWORD);
        var user = userService.findByEmail(initiateRequest.email());
        userService.updateUserStatus(user.getEmail(), Status.RESET);
        emailService.sendResetPasswordOtp(user.getEmail(), user.getFirstName(), otpToSend);
        return ResponseEntity.ok("If your email is registered, you will receive an email with instructions to reset your password.");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getLoggedInUser() {
        return ResponseEntity.ok(userService.getCurrentLoggedInUser());
    }


    @PatchMapping("/reset-password")
    @RateLimiter(name = "auth-rate-limiter")
    ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordRequest){
        if (!otpService.verifyOtp(resetPasswordRequest.email(), resetPasswordRequest.otp(), OtpType.FORGOT_PASSWORD)) {
            throw new BadRequestException("Invalid email or OTP");
        }

        // Change password
        userService.changeUserPassword(resetPasswordRequest.email(), resetPasswordRequest.newPassword());

        // Fetch user
        var user = userService.findByEmail(resetPasswordRequest.email());

        userService.updateUserStatus(user.getEmail(), Status.ACTIVE);


        // Send success email
        emailService.sendResetPasswordSuccessEmail(user.getEmail(), user.getFirstName());

        return ResponseEntity.ok("Password reset went successfully. You can login with your new password.");
    }


    @PostMapping("/login")
    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        var loginResult = authService.login(loginRequestDto, response);
        return ResponseEntity.ok(new LoginResponse(loginResult.accessToken()));
    }



}
