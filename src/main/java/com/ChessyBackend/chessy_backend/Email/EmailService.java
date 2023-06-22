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

    void resetOTP(OtpDTO otpDTO){

    }

    void verifyOTP(OtpVerifyDTO otpVerifyDTO){

    }

    public void testing(){
//        OtpDTO test = new OtpDTO("Sky","minhdev2002@gmail.com",false,otpService.generateOTP());
//        otpRepository.createOTP(test);

//        OtpVerifyDTO otpVerifyDTO = new OtpVerifyDTO("Sky","minhdev2002@gmail.com","287826 21-6-2023 11:13:29");
//        String result = otpRepository.isOTPValid(otpVerifyDTO);
//        System.out.println(result);

//        otpRepository.setVerifiedUser("Sky");
    }
}
