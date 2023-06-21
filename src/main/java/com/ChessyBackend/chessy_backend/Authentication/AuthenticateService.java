package com.ChessyBackend.chessy_backend.Authentication;

import com.ChessyBackend.chessy_backend.Authentication.ResponseEntity.RegisterResponse;
import com.ChessyBackend.chessy_backend.Email.EmailService;
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
            return new RegisterResponse("Register Success");
        }
    }

    public String validateRegisterUser(String username,String otp){
        String result = "";

        return result;
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

    public String testing(String username){
//        String token = tokenService.generateAccesToken(username);
//        String return_username = tokenService.getUsernameFromToken(token);
//        Date date_expired = tokenService.getExpirationDateFromToken(token);
//        return token;

        return userRepository.checkOTPUser("Sky","1234");
    }

}
