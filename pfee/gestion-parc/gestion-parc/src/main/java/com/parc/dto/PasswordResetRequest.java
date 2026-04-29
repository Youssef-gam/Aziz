package com.parc.dto;

public class PasswordResetRequest {
    private String newPassword;

    public PasswordResetRequest() {}

    public PasswordResetRequest(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
