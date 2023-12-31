package com.ChessyBackend.chessy_backend.User;


import com.ChessyBackend.chessy_backend.Token.TokenModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.JobKOctets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserRepository {

    public void addUser(UserModel userModel){
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("users").document(userModel.getUsername());

        Map<String, Object> data = new HashMap<>();
        data.put("username", userModel.getUsername());
        data.put("password", userModel.getPassword());
        data.put("email", userModel.getEmail());
        data.put("name",userModel.getName());
        data.put("avatarURL",userModel.getAvatarURL());
        ApiFuture<WriteResult> result = docRef.set(data);

    }

    //Update verify_users otp field
    //Constraint: Phải kểm tra xem username có nằm trong DB không?
    public String setUserNewOTP(String username, String new_otp){
        String result = "";
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docReg = db.collection("verify_users").document(username);
        ApiFuture<WriteResult> future = docReg.update("otp",new_otp);
        try {
            WriteResult writeResult = future.get();
            result = writeResult.toString();
        }
        catch (Exception e){
            result = e.getMessage();
        }
        return result;
    }


    public boolean checkUserIsInDB(String username) throws Exception{

        boolean result = false;

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference usersRef = db.collection("users");
        Query query = usersRef.whereEqualTo("username", username);

        //Call FireStone API to search for the user with username
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> match_users = querySnapshot.get().getDocuments();

        System.out.println(match_users.size());
        if(match_users != null && match_users.size() > 0){
            result = true;
        }

        return result;
    }

    public Boolean checkUsernamePassword(String username, String password) throws Exception {
        Boolean result = false;
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference usersRef = db.collection("users");
        Query query = usersRef.whereEqualTo("username", username).whereEqualTo("password",password);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> match_users = querySnapshot.get().getDocuments();
        if(match_users != null && match_users.size() > 0){
            result = true;
        }

        return result;
    }


    public UserModel findUserByUsername(String username){
        UserModel userModel = null;

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docReg = db.collection("users").document(username);
        ApiFuture<DocumentSnapshot> future = docReg.get();

        try{
            userModel = future.get().toObject(UserModel.class);
            userModel.setPassword("NULL");
        }
        catch (Exception e){
            userModel = null;
        }

        return userModel;
    }

    public String updateUser(UserModel userModel){
        String result = "";
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docReg = db.collection("users").document(userModel.getUsername());

        ApiFuture<WriteResult> future_email = docReg.update("email",userModel.getEmail());
        ApiFuture<WriteResult> future_name = docReg.update("email",userModel.getName());
        ApiFuture<WriteResult> future_avatar = docReg.update("email",userModel.getAvatarURL());
        try{
            WriteResult writeResult_email = future_email.get();
            WriteResult writeResult_name = future_name.get();
            WriteResult writeResult_avatar = future_avatar.get();
            result = writeResult_email.toString() + writeResult_avatar.toString() +
                    writeResult_name.toString();
        }
        catch (Exception e){
            result = e.getMessage();
        }
        return result;
    }

    public String updateUserEmail(String username, String newEmail){
        String result = "";
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docReg = db.collection("users").document(username);
        ApiFuture<WriteResult> future = docReg.update("email",newEmail);
        try{
            WriteResult writeResult = future.get();
            result = writeResult.toString();
        }
        catch (Exception e){
            result = e.getMessage();
        }
        return result;
    }

    public String updateUserName(String username, String newName){
        String result = "";

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docReg = db.collection("users").document(username);
        ApiFuture<WriteResult> future = docReg.update("name", newName);
        try{
            WriteResult writeResult = future.get();
            result = writeResult.toString();
        }
        catch (Exception e){
            result = e.getMessage();
        }
        return result;
    }

    public String updateUserAvatar(String username, String newAvatar){
        String result = "";

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docReg = db.collection("users").document(username);
        ApiFuture<WriteResult> future = docReg.update("avatarURL", newAvatar);
        try{
            WriteResult writeResult = future.get();
            result = writeResult.toString();
        }
        catch (Exception e){
            result = e.getMessage();
        }
        return result;
    }

    public String changeUserpassword(String username, String newPassword){
        String result = "";
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docReg = db.collection("users").document(username);
        ApiFuture<WriteResult> future = docReg.update("password", newPassword);
        try{
            WriteResult writeResult = future.get();
            result = writeResult.toString();
        }
        catch (Exception e){
            result = e.getMessage();
        }
        return result;
    }
}
