package com.ChessyBackend.chessy_backend.User;

import com.ChessyBackend.chessy_backend.Email.DTO.OtpDTO;
import com.ChessyBackend.chessy_backend.Email.EmailService;
import com.ChessyBackend.chessy_backend.Email.OTPRepository;
import com.ChessyBackend.chessy_backend.Email.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    OTPRepository otpRepository;
    @Autowired
    OTPService otpService;

    @Autowired
    EmailService emailService;

    public UserModel getUser(String username){
        return null;
    }

    public String updateUser(UserModel userModel){
        String result = "";

        return result;
    }
    public String updateUser(UserModel userModel,String attribute){
        String result = "";
        if(attribute.equals("email")){
            result = updateUserEmail(userModel.getUsername(),userModel.getEmail());
        }
        else if(attribute.equals("avatar")){
            result = updateUserAvatar(userModel.getUsername(),userModel.getAvatarURL());
        }
        else if(attribute.equals("name")){
            result = updateUserName(userModel.getUsername(),userModel.getName());
        }
        else if(attribute.equals("password")){
            String oldPassword = userModel.getPassword();
            //Tricked in sending in email field
            String newPassword = userModel.getEmail();
            result = updatePassword(userModel.getUsername(),oldPassword,newPassword);
        }
        else{
            result = "Invalid attribute";
        }
        return result;
    }

    private String updateUserEmail(String username ,String email){
        return userRepository.updateUserEmail(username,email);
    }

    private String updateUserAvatar(String username, String avatarLink){
        return userRepository.updateUserAvatar(username,avatarLink);
    }

    private String updateUserName(String username, String name){
        return userRepository.updateUserName(username,name);
    }

    private String updatePassword(String username,String oldPassword,String newPassword){
        //Check userNamePassword valid
        String result = "";

        try{
            Boolean isUsernamePasswordValid = userRepository.
                    checkUsernamePassword(username,oldPassword);
            if(!isUsernamePasswordValid){
                result = "Current Password is not valid!";
            }
            else{
                //change password in Database
                result = userRepository.changeUserpassword(username,newPassword);
                //set acc này thành un-verified
                otpRepository.unverifyUser(username);


                //Thay đổi OTP trong database
                //Search user hiện tại lên
                UserModel userModel = userRepository.findUserByUsername(username);

                String newOTP = otpService.generateOTP();
                OtpDTO otpDTO = new OtpDTO(username,userModel.getEmail(),false,newOTP);
                otpRepository.regenerateOTP(otpDTO);
                //Gửi OTP xác nhận
                emailService.sendEmailOTP(otpDTO);

            }
        }
        catch (Exception e){
            result = e.getMessage();
        }



        return result;
    }
}
