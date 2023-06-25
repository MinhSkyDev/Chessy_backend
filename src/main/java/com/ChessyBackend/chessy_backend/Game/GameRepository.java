package com.ChessyBackend.chessy_backend.Game;

import com.ChessyBackend.chessy_backend.Game.DTO.GameHistory;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
}
