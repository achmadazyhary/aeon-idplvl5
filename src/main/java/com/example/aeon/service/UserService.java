package com.example.aeon.service;

import com.example.aeon.dao.LoginModel;
import com.example.aeon.model.RegisterModel;

import java.util.Map;

public interface UserService {
    Map registerManual(RegisterModel objModel);
    Map login(LoginModel objLogin);
}
