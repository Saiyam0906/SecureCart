package com.example.Ecommerce.Interface;

public interface LoginAttemptInterface {
	void loginSucceeded(String email);
    void loginFailed(String email);
    boolean isBlocked(String email);
    long getRemainingLockTime(String email);

}
