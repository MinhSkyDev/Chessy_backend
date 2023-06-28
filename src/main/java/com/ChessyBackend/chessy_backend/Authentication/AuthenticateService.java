package com.ChessyBackend.chessy_backend.Authentication;

import com.ChessyBackend.chessy_backend.Authentication.ResponseEntity.RegisterResponse;
import com.ChessyBackend.chessy_backend.Email.DTO.OtpDTO;
import com.ChessyBackend.chessy_backend.Email.DTO.OtpVerifyDTO;
import com.ChessyBackend.chessy_backend.Email.EmailService;
import com.ChessyBackend.chessy_backend.Email.OTPRepository;
import com.ChessyBackend.chessy_backend.Email.OTPService;
import com.ChessyBackend.chessy_backend.Token.TokenModel;
import com.ChessyBackend.chessy_backend.Token.TokenRepository;
import com.ChessyBackend.chessy_backend.Token.TokenService;
import com.ChessyBackend.chessy_backend.User.UserModel;
import com.ChessyBackend.chessy_backend.User.UserRepository;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



@Service
public class AuthenticateService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TokenRepository tokenRepository;

    @Autowired
    public TokenService tokenService;

    @Autowired
    public EmailService emailService;

    @Autowired
    OTPRepository otpRepository;

    @Autowired
    OTPService otpService;

    public RegisterResponse registerUser(UserModel model) throws Exception{

        //Kiểm tra tài khoản có trong DB hay không?
        boolean isInDB = userRepository.checkUserIsInDB(model.getUsername());

        System.out.println(isInDB);
        if(isInDB){
            return new RegisterResponse("User is already in Database");
        }
        else{
            //Lưu tài khoản vào DB bảng users
            userRepository.addUser(model);
            //Lưu tài khoản vào DB bảng verify_users

            String otpWithDate = otpService.generateOTP();
            OtpDTO otpDTO = new OtpDTO(model.getUsername(), model.getEmail(), false , otpWithDate);

            //Lưu OTP xuống DB
            otpRepository.createOTP(otpDTO);

            //Gửi email
            emailService.sendEmailOTP(otpDTO);

            return new RegisterResponse("Register Success");
        }
    }

    //Hàm này dùng để kiểm tra OTP người dùng gửi tới
    public RegisterResponse verifyOTP(OtpVerifyDTO otpVerifyDTO){
        String result = otpRepository.isOTPValid(otpVerifyDTO);
        if(result.equals("Equals")){
            otpRepository.setVerifiedUser(otpVerifyDTO.getUsername());
        }
        return new RegisterResponse(result);
    }

    public RegisterResponse checkIsUserVerify(String username){
        String result = "";
        result = otpRepository.checkVerify(username);
        return new RegisterResponse(result);
    }

    public RegisterResponse regenerateOTP(OtpVerifyDTO otpVerifyDTO){
        OtpDTO newOtpDTO = new OtpDTO(otpVerifyDTO.getUsername(), otpVerifyDTO.getEmail(), false, otpService.generateOTP());
        String result = otpRepository.regenerateOTP(newOtpDTO);
        if(result.equals("RegeneratedSuccess")){
            emailService.resetOTP(newOtpDTO);
        }

        return new RegisterResponse(result);
    }



    public ResponseEntity<TokenModel> loginUser(String username, String password) throws Exception{
        //Kiểm tra username có trong DB hay không?
        Boolean isUsernamePasswordValid = userRepository.checkUsernamePassword(username,password);

        if(!isUsernamePasswordValid){
            TokenModel failTokenModel = new TokenModel("NULL","NULL","Password is not valid");
            return new ResponseEntity<>(failTokenModel, HttpStatus.CONFLICT);
        }
        else{
            //Kiểm tra user đã có token chưa? if chưa thì tạo
            if(!tokenRepository.checkTokenCreated(username)){
                TokenModel tokenModel = tokenRepository.createToken(username);
                return new ResponseEntity<>(tokenModel,HttpStatus.OK);
            }
            else{
                //Kiểm tra token hết hạn hay chưa?
                Boolean isRefreshTokenExpired = tokenRepository.checkRefreshTokenExpiration(username);
                System.out.println(isRefreshTokenExpired);
                if(isRefreshTokenExpired){
                    //Recreate token
                    TokenModel newTokenModel = tokenRepository.regenerateToken(username);
                    return new ResponseEntity<>(newTokenModel, HttpStatus.OK);
                }
                else{
                    TokenModel tokenModel = tokenRepository.getToken(username);
                    return new ResponseEntity<>(tokenModel,HttpStatus.OK);
                }
            }
        }
    }



}
