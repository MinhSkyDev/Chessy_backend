package com.ChessyBackend.chessy_backend.Email;

import com.ChessyBackend.chessy_backend.Email.DTO.OtpDTO;
import com.ChessyBackend.chessy_backend.Email.DTO.OtpVerifyDTO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firestore.v1.Write;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OTPRepository {

    @Autowired
    OTPService otpService;
    public void createOTP(OtpDTO otpDTO){
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("verify_users").document(otpDTO.getUsername());

        Map<String, Object> data = new HashMap<>();
        data.put("username", otpDTO.getUsername());
        data.put("email", otpDTO.getEmail());
        data.put("isVerify",false);
        data.put("otp",otpDTO.getOtp());
        ApiFuture<WriteResult> result = docRef.set(data);
    }

    public String regenerateOTP(OtpDTO newOtpDTO){
        String result = "";
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docReg = db.collection("verify_users").document(newOtpDTO.getUsername());
        ApiFuture<WriteResult> future = docReg.update("otp",newOtpDTO.getOtp());
        try {
            WriteResult writeResult = future.get();
            result = "RegeneratedSuccess";
        }
        catch (Exception e){
            result = e.getMessage();
        }
        return result;
    }



    public String isOTPValid(OtpVerifyDTO otpVerifyDTO){
        //Query user
        String result = "";
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference usersRef = db.collection("verify_users");
        Query query = usersRef.whereEqualTo("username", otpVerifyDTO.getUsername());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        OtpDTO userVerifyModel = null;
        try{
            List<QueryDocumentSnapshot> match_users = querySnapshot.get().getDocuments();
            userVerifyModel = match_users.get(0).toObject(OtpDTO.class);

            String otp_With_Date = userVerifyModel.getOtp();

            String otp = otpService.extractOTP(otp_With_Date);
            Date expiredDate = otpService.extractDateFromOTP(otp_With_Date);
            Date currentDate = new Date(System.currentTimeMillis());
            if(otp.equals(otpVerifyDTO.getOtp()) && expiredDate.compareTo(currentDate) < 0){
                result = "Equals";
            }
            else{
                result = "NotEquals";
            }
        }
        catch (Exception e){
            result = e.getMessage();
        }

        return result;
    }


    //Update verify_users isVerify field
    //Constraint: Phải kểm tra xem username có nằm trong DB không?
    public String setVerifiedUser(String username){
        String result = "";
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docReg = db.collection("verify_users").document(username);
        ApiFuture<WriteResult> future = docReg.update("isVerify",true);
        try {
            WriteResult writeResult = future.get();
            result = writeResult.toString();
        }
        catch (Exception e){
            result = e.getMessage();
        }
        return result;
    }


}
