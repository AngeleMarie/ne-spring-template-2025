package rw.rra.management.vehicles.email;

import rw.rra.management.vehicles.auth.OtpType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final ITemplateEngine templateEngine;

    @Async
    public void sendAccountVerificationEmail(String to, String name, String otp){
        sendOtpEmail(to, name, otp, OtpType.VERIFY_ACCOUNT);
    }

    @Async
    public void sendResetPasswordOtp(String to, String name, String otp){
        sendOtpEmail(to, name, otp, OtpType.FORGOT_PASSWORD);
    }

    @Async
    public void sendVerificationSuccessEmail(String to, String name) {
        sendSuccessEmail(to, name, "verify_success", "Account Verified Successfully");
    }

    @Async
    public void sendResetPasswordSuccessEmail(String to, String name) {
        sendSuccessEmail(to, name, "reset_success", "Password Reset Successfully");
    }
    @Async
    public void sendUserRegisteredEmail(String to, String name,String plateNumber) {
        sendGenericEmail(to, name, plateNumber,"owner_registered", "Welcome to RRA Vehicle Management");
    }

    @Async
    public void sendPlateCreatedEmail(String to, String name, String plateNumber) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("plateNumber", plateNumber);
        context.setVariable("companyName", "Rwanda Revenue Authority");
        sendEmail("plate_created", context, to, "Your Plate Number Has Been Created");
    }

    @Async
    public void sendPlateAssignedEmail(String to, String name, String plateNumber, String vehicleDetails) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("plateNumber", plateNumber);
        context.setVariable("vehicleDetails", vehicleDetails);
        context.setVariable("companyName", "Rwanda Revenue Authority");
        sendEmail("plate_assigned", context, to, "Your Plate Has Been Assigned to a Vehicle");
    }

    @Async
    public void sendOwnershipTransferEmailToSender(String to, String name, String plateNumber, Double amount, String newOwnerFirstName, String newOwnerLastName) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("plateNumber", plateNumber);
        context.setVariable("amount", amount);
        context.setVariable("newOwner", newOwnerFirstName + " " + newOwnerLastName);
        context.setVariable("companyName", "Rwanda Revenue Authority");
        sendEmail("ownership_transferred_sender", context, to, "Vehicle Ownership Transferred");
    }

    @Async
    public void sendPostInspectionNotification(String to, String name, String plateNumber) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("plateNumber", plateNumber);
        context.setVariable("companyName", "Rwanda Revenue Authority");

        sendEmail("post_inspection_notification", context, to, "Vehicle Inspection Update");
    }


    @Async
    public void sendOwnershipTransferEmailToReceiver(String to, String name, String plateNumber, Double amount, String previousOwnerFirstName, String previousOwnerLastName) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("plateNumber", plateNumber);
        context.setVariable("amount", amount);
        context.setVariable("previousOwner", previousOwnerFirstName + " " + previousOwnerLastName);
        context.setVariable("companyName", "Rwanda Revenue Authority");
        sendEmail("ownership_transferred_receiver", context, to, "You Received a Vehicle Ownership");
    }


    private void sendOtpEmail(String to, String name, String otp, OtpType otpType){
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("otp", otp);
            context.setVariable("companyName", "Rwanda Revenue Authority");
            context.setVariable("expirationTime", "10");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED);
            String process = "";
            String templateName = "";

            switch(otpType) {
                case VERIFY_ACCOUNT -> {
                    templateName = "verify_account";
                    process = templateEngine.process(templateName, context);
                    helper.setSubject("Verify your account - One Time Password (OTP)");
                }
                case FORGOT_PASSWORD -> {
                    templateName = "forgot_password";
                    process = templateEngine.process(templateName, context);
                    helper.setSubject("Reset your password - One Time Password (OTP)");
                }
                default -> log.error("Invalid otp type detected!");
            }

            helper.setText(process, true);
            helper.setTo(to);
            helper.setFrom("noreply@rra.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Unable to send the email", e);
        }
    }

    private void sendSuccessEmail(String to, String name, String templateName, String subject) {
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("companyName", "Rwanda Revenue Authority");

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED);

            String process = templateEngine.process(templateName, context);
            helper.setSubject(subject);
            helper.setText(process, true);
            helper.setTo(to);
            helper.setFrom("noreply@rra.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Unable to send success email", e);
        }
    }
    private void sendGenericEmail(String to, String name, String plateNumber,String template, String subject) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("plateNumber", plateNumber);
        context.setVariable("companyName", "Rwanda Revenue Authority");
        sendEmail(template, context, to, subject);
    }

    private void sendEmail(String templateName, Context context, String to, String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED);
            String content = templateEngine.process(templateName, context);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom("noreply@rra.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email [{}] to {}", subject, to, e);
        }
    }

}
