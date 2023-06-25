package com.ChessyBackend.chessy_backend.Game;

import com.ChessyBackend.chessy_backend.Game.DTO.GameHistory;
import com.ChessyBackend.chessy_backend.Game.DTO.GameModel;
import com.ChessyBackend.chessy_backend.Game.DTO.GameStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Lớp này sử dụng Singleton DesignPattern

public class GameStorage {
    private static Map<String, GameModel> currentGames;
    private static Map<String, GameHistory> history;
    private static GameStorage instance;



    private GameStorage(){
        currentGames = new HashMap<>();
        history = new HashMap<>();
    }

    public static GameStorage getInstance(){
        if(instance == null){
            instance = new GameStorage();
        }
        return instance;
    }

    public Map<String,GameModel> getGames(){
        return currentGames;
    }
    public Map<String,GameHistory> getHistory(){return history;}

    public void registerHistory(GameHistory gameHistory){
        history.put(gameHistory.getGameID(), gameHistory);
    }

    public void registerNewGame(GameModel gameModel){
        currentGames.put(gameModel.getGameId(), gameModel);
//        GameHistory newHistory = new GameHistory(new ArrayList<GameStatusDTO>());
//        history.put(gameModel.getGameId(), newHistory);
    }

    public void finishGame(String gameID){
        currentGames.remove(gameID);
        currentGames.remove(gameID);
    }


}
