package com.example.SecureCart.Interface;

public interface EmailInterface {
	void sendLoginFailureAlert(String email, int failedAttempts, int maxAttempts);
    void sendAccountLockedNotification(String email, long lockDurationMinutes);
    void sendPasswordChangeConfirmation(String email);
    void sendOrderConfirmation(String email, String orderNumber, double totalAmount);
    void sendEmailverification(String email);
    boolean verifyEmail(String token);
    boolean isEmailVerified(String email);
    void resendVerificationEmail(String email);
}
