package com.example.aeon.controller;

import com.example.aeon.config.Config;
import com.example.aeon.dao.LoginModel;
import com.example.aeon.repository.oauth.UserRepository;
import com.example.aeon.service.UserService;
import com.example.aeon.utils.TemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/user-login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    Config config = new Config();

    @Autowired
    public UserService serviceReq;

    @Autowired
    public TemplateResponse templateCRUD;

    @PostMapping("/login")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map> login(@Valid @RequestBody LoginModel objModel) {
        Map map = serviceReq.login(objModel);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

}
