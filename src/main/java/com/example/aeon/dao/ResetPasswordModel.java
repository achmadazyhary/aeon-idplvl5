package com.example.aeon.dao;

import lombok.Data;

@Data
public class ResetPasswordModel {
    public String username;
    public String otp;
    public String newPassword;
}
