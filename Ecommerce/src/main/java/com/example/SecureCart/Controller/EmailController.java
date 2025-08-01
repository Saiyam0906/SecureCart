package com.example.SecureCart.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SecureCart.Service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {
	
	private final EmailService emailService;
	
	@GetMapping("/send-login-failure")
    public String sendLoginFailure(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int failedAttempts,
            @RequestParam(defaultValue = "1") int maxAttempts) {
        
        emailService.sendLoginFailureAlert(email, failedAttempts, maxAttempts);
        return "Login failure alert email sent to " + email;
    }

    // 2️⃣ Test Account Locked
    @GetMapping("/send-account-locked")
    public String sendAccountLocked(
            @RequestParam String email,
            @RequestParam(defaultValue = "15") long lockDurationMinutes) {
        
        emailService.sendAccountLockedNotification(email, lockDurationMinutes);
        return "Account locked email sent to " + email;
    }

    // 3️⃣ Test Password Change Confirmation
    @GetMapping("/send-password-change")
    public String sendPasswordChange(@RequestParam String email) {
        emailService.sendPasswordChangeConfirmation(email);
        return "Password change confirmation email sent to " + email;
    }

    // 4️⃣ Test Order Confirmation
    @GetMapping("/send-order-confirmation")
    public String sendOrderConfirmation(
            @RequestParam String email,
            @RequestParam(defaultValue = "ORD12345") String orderNumber,
            @RequestParam(defaultValue = "00.00") double totalAmount) {
        
        emailService.sendOrderConfirmation(email, orderNumber, totalAmount);
        return "Order confirmation email sent to " + email;
    }
	

}
