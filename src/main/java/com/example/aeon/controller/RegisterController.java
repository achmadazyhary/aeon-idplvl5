package com.example.aeon.controller;

import com.example.aeon.config.Config;
import com.example.aeon.model.RegisterModel;
import com.example.aeon.model.oauth.User;
import com.example.aeon.repository.oauth.UserRepository;
import com.example.aeon.service.UserService;
import com.example.aeon.service.email.EmailSender;
import com.example.aeon.utils.EmailTemplate;
import com.example.aeon.utils.SimpleStringUtils;
import com.example.aeon.utils.TemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user-register")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    Config config = new Config();

    @Autowired
    public UserService serviceReq;

    @Autowired
    public TemplateResponse templateCRUD;

    @Autowired
    public EmailTemplate emailTemplate;

    @Autowired
    public EmailSender emailSender;

    @Value("${expired.token.password.minute:}")//FILE_SHOW_RUL
    private int expiredToken;

    @PostMapping("/")
    public ResponseEntity<Map> saveRegister(@Valid @RequestBody RegisterModel objModel) {
        Map map = new HashMap();

        User user = userRepository.checkExistingEmail(objModel.getUsername());
        if (null != user) {
            return new ResponseEntity<Map>(templateCRUD.notFound("Username sudah ada"), HttpStatus.OK);
        }

        map = serviceReq.registerManual(objModel);
        Map sendOTP = sendEmailRegister(objModel);

        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    // Step 2: sendp OTP berupa URL: guna updeta enable agar bisa login:
    @PostMapping("/send-otp")
    public Map sendEmailRegister(@RequestBody RegisterModel user) {
        String message = "Thanks, please check your email for activation.";

        if (user.getUsername() == null) return templateCRUD.templateError("No email provided");
        User found = userRepository.findOneByUsername(user.getUsername());
        if (found == null) return templateCRUD.notFound("Email not found"); //throw new BadRequest("Email not found");

        String template = emailTemplate.getRegisterTemplate();
        if (StringUtils.isEmpty(found.getOtp())) {
            User search;
            String otp;
            do {
                otp = SimpleStringUtils.randomString(6, true);
                search = userRepository.findOneByOTP(otp);
            } while (search != null);
            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();

            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getFullname() == null ? found.getUsername1() : found.getFullname()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", otp);
            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getFullname() == null ? found.getUsername1() : found.getFullname()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", found.getOtp());
        }
        emailSender.sendAsync(found.getUsername(), "Register", template);
        return templateCRUD.templateSukses(message);

    }

    @GetMapping("/register-confirm-otp/{token}")
    public ResponseEntity<Map> registerConfirmOtp(@PathVariable(value = "token") String tokenOtp, @RequestBody RegisterModel objModel) {
        User user = userRepository.findOneByOTPandUsername(objModel.getUsername(), tokenOtp);
        if (null == user) {
            return new ResponseEntity<Map>(templateCRUD.templateError("OTP dan Username tidak ditemukan"), HttpStatus.OK);
        }
        if(user.isEnabled()){
            return new ResponseEntity<Map>(templateCRUD.templateSukses("Akun Anda sudah aktif, Silahkan melakukan login"), HttpStatus.OK);
        }
        String today = config.convertDateToString(new Date());
        String dateToken = config.convertDateToString(user.getOtpExpiredDate());

        if(Long.parseLong(today) > Long.parseLong(dateToken)){
            return new ResponseEntity<Map>(templateCRUD.templateError("Your token is expired. Please Get token again."), HttpStatus.OK);
        }
        //update user
        user.setEnabled(true);
        userRepository.save(user);
        return new ResponseEntity<Map>(templateCRUD.templateSukses("Sukses, Silahkan Melakukan Login"), HttpStatus.OK);
    }

}
