package com.example.SecureCart.Interface;

import com.example.SecureCart.model.PasswordResetToken;
import com.example.SecureCart.model.User;

public interface ForgotPasswordInterface {

	public void initiatePasswordReset(String email);
	 public PasswordResetToken validateResetToken(String token);
	 public void resetPassword(String token, String newPassword);
	 
	 
}
