package com.ChessyBackend.chessy_backend.Email;

import com.ChessyBackend.chessy_backend.Email.DTO.OTPResponse;
import com.ChessyBackend.chessy_backend.Email.DTO.OtpDTO;
import com.ChessyBackend.chessy_backend.Email.DTO.OtpVerifyDTO;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class OTPService {
    public String generateOTP(){
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String otp = Integer.toString(randomNumber);
        while(otp.length() < 6){
            otp = "0"+otp;
        }

        Date otp_expired_date = new Date(System.currentTimeMillis() +600000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String expiredDateString = dateFormat.format(otp_expired_date);
        return otp +" "+expiredDateString;
    }

    public String extractOTP(String otp){
        return otp.substring(0,6);
    }



    public Date extractDateFromOTP(String otp) throws  Exception{

        String date_String = otp.substring(7);

        Date otp_expired = new SimpleDateFormat("dd-M-yyyy hh:mm:ss").parse(date_String);
        return otp_expired;
    }
}
