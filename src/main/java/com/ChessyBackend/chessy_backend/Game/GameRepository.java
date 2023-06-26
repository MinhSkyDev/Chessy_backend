package com.ChessyBackend.chessy_backend.Game;

import com.ChessyBackend.chessy_backend.Game.DTO.GameHistory;
import com.ChessyBackend.chessy_backend.Token.TokenDTO;
import com.ChessyBackend.chessy_backend.Token.TokenModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameRepository {

    public void saveHistory(GameHistory gameHistory){
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> docData = new HashMap<>();

        docData.put("gameID", gameHistory.getGameID());
        docData.put("player1", gameHistory.getPlayer1());
        docData.put("player2", gameHistory.getPlayer2());
        docData.put("history", gameHistory.getHistory());
        System.out.println(gameHistory.getGameID());
        ApiFuture<WriteResult> future = db.collection("game_history").document(gameHistory.getGameID()).set(docData);



    }

    public ArrayList<GameHistory> getHistory(String username) {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference usersRef = db.collection("game_history");
        Query query1 = usersRef.whereEqualTo("player1",username);
        Query query2 = usersRef.whereEqualTo("player2",username);
        ApiFuture<QuerySnapshot> querySnapshot1 = query1.get();
        ApiFuture<QuerySnapshot> querySnapshot2 = query2.get();

        ArrayList<GameHistory> histories = new ArrayList<>();

        try {
            List<QueryDocumentSnapshot> match_histories1 = querySnapshot1.get().getDocuments();
            for (QueryDocumentSnapshot documentSnapshot : match_histories1) {
                histories.add(documentSnapshot.toObject(GameHistory.class));
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        try {
            List<QueryDocumentSnapshot> match_histories2 = querySnapshot2.get().getDocuments();
            for (QueryDocumentSnapshot documentSnapshot : match_histories2) {
                histories.add(documentSnapshot.toObject(GameHistory.class));
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return histories;
    }
}
