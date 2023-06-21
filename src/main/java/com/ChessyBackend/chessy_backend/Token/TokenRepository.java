package com.ChessyBackend.chessy_backend.Token;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class TokenRepository {
    
    @Autowired
    TokenService tokenService;

    public Boolean checkRefreshTokenExpiration(String username) {
        Boolean result = false;
        //Get TokenDTO from username
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference usersRef = db.collection("tokens");
        Query query = usersRef.whereEqualTo("username", username);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        TokenDTO tokenDTO = null;
        try{
            List<QueryDocumentSnapshot> match_users = querySnapshot.get().getDocuments();
            tokenDTO = match_users.get(0).toObject(TokenDTO.class);
            Date current_refresh = tokenService.getExpirationDateFromToken(tokenDTO.getRefresh_token());
            Date current = new Date(System.currentTimeMillis());
            //Date của refresh mà phía trước current thì token hết hạn
            if(current_refresh.compareTo(current) < 0){
                //Trả về -1 tức là current_refresh đã xảy ra trước current
                //Trong khi current_refresh cần xảy ra sau current (vì ở thời gian xa hơn)
                result = true;
            }
        }
        catch(Exception e){
            result = true;
            return result;
        }

        return  result;
    }

    //Update query
    public TokenModel regenerateToken(String username) {

        //Regenate new Token
        String accessToken = tokenService.generateAccesToken(username);
        String refreshToken = tokenService.generateRefreshToken(username);

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docReg = db.collection("tokens").document(username);
        ApiFuture<WriteResult> future_access = docReg.update("access_token",accessToken);
        ApiFuture<WriteResult> future_refresh = docReg.update("refresh_token",refreshToken);

        try{
            WriteResult result_access = future_access.get();
            WriteResult result_refresh = future_refresh.get();

            return new TokenModel(accessToken,refreshToken,result_access.toString() + result_refresh.toString());
        }
        catch(Exception e){
            return new TokenModel("NULL","NULL",e.getMessage());
        }
    }

    public TokenModel createToken(String username) {
        //Khởi tạo token cho username
        String accessToken = tokenService.generateAccesToken(username);
        String refreshToken = tokenService.generateRefreshToken(username);

        //Save into DB
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("tokens").document(username);

        HashMap<String, Object> data = new HashMap<>();
        data.put("username",username);
        data.put("access_token",accessToken);
        data.put("refresh_token", refreshToken);

        ApiFuture<WriteResult> result = docRef.set(data);
        TokenModel tokenModel = new TokenModel(accessToken,refreshToken,"Created Token");
        return tokenModel;

    }

    public TokenModel getToken(String username) {
        Firestore db = FirestoreClient.getFirestore();

        CollectionReference usersRef = db.collection("tokens");
        Query query = usersRef.whereEqualTo("username", username);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        TokenDTO tokenDTO = null;
        try{
            List<QueryDocumentSnapshot> match_users = querySnapshot.get().getDocuments();
            tokenDTO = match_users.get(0).toObject(TokenDTO.class);
            return new TokenModel(tokenDTO.getAccess_token(),tokenDTO.getRefresh_token(),"Success");
        }
        catch (Exception e){
            return new TokenModel("NULL","NULL",e.getMessage());
        }

    }

    public Boolean checkTokenCreated(String username) throws  Exception {
        Boolean result = false;
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference usersRef = db.collection("tokens");
        Query query = usersRef.whereEqualTo("username", username);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> match_users = querySnapshot.get().getDocuments();
        if(match_users != null && match_users.size() > 0){
            result = true;
        }

        return result;
    }



}
