package com.ChessyBackend.chessy_backend.Authentication;


import com.ChessyBackend.chessy_backend.Authentication.ResponseEntity.RegisterResponse;
import com.ChessyBackend.chessy_backend.Email.DTO.OtpVerifyDTO;
import com.ChessyBackend.chessy_backend.Email.EmailService;
import com.ChessyBackend.chessy_backend.Token.TokenModel;
import com.ChessyBackend.chessy_backend.User.RequestDTO.UserEmailDTO;
import com.ChessyBackend.chessy_backend.User.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authenticate")
public class AuthenticateController {

    @Autowired
    AuthenticateService authenticateService;

    @Autowired
    EmailService emailService;


    @PostMapping("/login")
    public ResponseEntity<TokenModel> login(@RequestBody AuthenticateDTO authenticateDTO){
        try {
            return authenticateService.loginUser(authenticateDTO.getUsername(), authenticateDTO.getPassword());
        }
        catch (Exception e){
            TokenModel failTokenModel = new TokenModel("NULL","NULL",e.getMessage());
            return new ResponseEntity<>(failTokenModel,HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody UserModel userModel){
        try{
        return new ResponseEntity<>(authenticateService.registerUser(userModel),HttpStatus.OK);
        }
        catch (Exception e){
            RegisterResponse failResponse = new RegisterResponse(e.getMessage());
            return new ResponseEntity<>(failResponse,HttpStatus.CONFLICT);
        }
    }



    @PostMapping("/forget")
    public void forgetPassword(@RequestBody UserModel userModel){

    }

    @PostMapping("/verify")
    public ResponseEntity<RegisterResponse> verifyOTP(@RequestBody OtpVerifyDTO otpVerifyDTO){
        return new ResponseEntity<RegisterResponse>(authenticateService.verifyOTP(otpVerifyDTO),HttpStatus.OK);
    }

    @PostMapping("/regenerate")
    public ResponseEntity<RegisterResponse> regenerateOTP(@RequestBody UserEmailDTO userEmailDTO){
        OtpVerifyDTO otpVerifyDTO = new OtpVerifyDTO(userEmailDTO.getUsername(), userEmailDTO.getEmail(), "NULL");
        return new ResponseEntity<RegisterResponse>(authenticateService.regenerateOTP(otpVerifyDTO),HttpStatus.OK);
    }

    @PostMapping("/isVerify")
    public ResponseEntity<RegisterResponse> checkUsernameVerify(@RequestBody AuthenticateDTO authenticateDTO){
        return new ResponseEntity<RegisterResponse>(
                authenticateService.checkIsUserVerify(authenticateDTO.getUsername()), HttpStatus.OK);
    }

    @GetMapping("/restricted")
    public String test2(){
        try{
        return "Enter Restricted";}
        catch (Exception e){
            return e.getMessage();
        }
    }

}
