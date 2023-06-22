package com.ChessyBackend.chessy_backend.Email;

import com.ChessyBackend.chessy_backend.Email.DTO.OtpDTO;
import com.ChessyBackend.chessy_backend.Email.DTO.OtpVerifyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    OTPService otpService;
    @Autowired
    OTPRepository otpRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailOTP(OtpDTO otpDTO){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("minhdev2002@gmail.com");
        message.setTo(otpDTO.getEmail());
        message.setSubject("Chessy OTP Verify");
        message.setText("Here is your OTP" + " " +otpService.extractOTP(otpDTO.getOtp()));

        javaMailSender.send(message);
        System.out.println("Email sent to "+otpDTO.getEmail());

    }

    public void resetOTP(OtpDTO otpDTO){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("minhdev2002@gmail.com");
        message.setTo(otpDTO.getEmail());
        message.setSubject("Chessy OTP Re-generated Verify");
        message.setText("Here is your Re-generated OTP" + " " +otpService.extractOTP(otpDTO.getOtp()));

        javaMailSender.send(message);
        System.out.println("Email re-sent to "+otpDTO.getEmail());
    }



}
