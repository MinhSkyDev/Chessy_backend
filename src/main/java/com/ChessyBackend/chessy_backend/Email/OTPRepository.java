package com.ChessyBackend.chessy_backend.Email;

import com.ChessyBackend.chessy_backend.Email.DTO.OtpDTO;
import com.ChessyBackend.chessy_backend.Email.DTO.OtpVerifyDTO;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class OTPRepository {

    void createOTP(OtpDTO otpDTO){

    }

    void regenerateOTP(OtpDTO otpDTO){

    }

    Boolean isOTPValid(OtpVerifyDTO otpVerifyDTO){
        return false;
    }




}
