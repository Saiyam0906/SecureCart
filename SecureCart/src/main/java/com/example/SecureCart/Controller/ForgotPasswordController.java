package com.example.SecureCart.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.SecureCart.Request.ForgotPasswordRequest;
import com.example.SecureCart.Request.ResetPasswordRequest;
import com.example.SecureCart.Response.ApiResponse;
import com.example.SecureCart.Response.TokenValidationResponse;
import com.example.SecureCart.Service.ForgotPasswordService;
import com.example.SecureCart.model.PasswordResetToken;
import com.example.SecureCart.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ForgotPasswordController {

	private final ForgotPasswordService forgotPasswordService;
	
	@PostMapping("/forgot-password")
	public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request){
		try {
			forgotPasswordService.initiatePasswordReset(request.getEmail());
			return ResponseEntity.ok(new ApiResponse("If an account with that email exists, we've sent password reset instructions.", request));
		}catch (Exception e) {
			log.error("Error in forgot password endpoint", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("An error occurred. Please try again later.", e));
		}
	}
	
	@GetMapping("/reset-password/validate")
	public ResponseEntity<ApiResponse> validateResetToken(@RequestParam String token){
		try {
            PasswordResetToken resetToken = forgotPasswordService.validateResetToken(token);
            User user = resetToken.getUser();

            TokenValidationResponse response = new TokenValidationResponse(
                    user.getEmail(),
                    user.getFirstName()
            );
            return ResponseEntity.ok(new ApiResponse(
                    "Token is valid.",
                    resetToken.getUser().getEmail()));
        } catch (Exception e) {
            log.error("Error validating reset token", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        }
	}
	
	@PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            forgotPasswordService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new ApiResponse(
                    "Password has been reset successfully. You can now login with your new password.",
                    null));
        } catch (Exception e) {
            log.error("Error resetting password", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
