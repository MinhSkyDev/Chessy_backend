package com.ChessyBackend.chessy_backend.Game;


import com.ChessyBackend.chessy_backend.Game.DTO.GameHistory;
import com.ChessyBackend.chessy_backend.Game.DTO.GameModel;
import com.ChessyBackend.chessy_backend.Game.DTO.GameStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    //Tạo game với usertạo
    public GameModel createGame(String userCreated){
        GameModel newGame = new GameModel();
        newGame.setPlayer1(userCreated);
        newGame.setStatus("OPEN");
        newGame.setGameId(UUID.randomUUID().toString());

        GameStorage.getInstance().registerNewGame(newGame);
        return newGame;
    }

    public GameModel connectGame(String player2, String gameID){
        if(!GameStorage.getInstance().getGames().containsKey(gameID)){
            return null;
        }
        else{
            GameModel gameModel = GameStorage.getInstance().getGames().get(gameID);
            gameModel.setPlayer2(player2);
            gameModel.setStatus("INPROGRESS");
            GameStorage.getInstance().registerNewGame(gameModel);

            GameStatusDTO sendGameStatus = new GameStatusDTO(gameID, gameModel.getPlayer1(),
                    gameModel.getPlayer2(),"NULL","NULL","Black","SYSTEM LEAVE");

            //Thông báo info của đối thủ qua cho mọi người
            simpMessagingTemplate.convertAndSend("/topic/game-progress/"+gameID,sendGameStatus);
            return gameModel;
        }
    }

    //Player2 leaveRoom
    public void leaveRoom(String player2, String gameID){
        if(!GameStorage.getInstance().getGames().containsKey(gameID)){
            return;
        }
        else{
            GameModel gameModel = GameStorage.getInstance().getGames().get(gameID);
            gameModel.setPlayer2("NULL");
            gameModel.setStatus("OPEN");
            GameStorage.getInstance().registerNewGame(gameModel);

            GameStatusDTO sendGameStatus = new GameStatusDTO(gameID, gameModel.getPlayer1(),
                    gameModel.getPlayer2(),"NULL","NULL","Black","SYSTEM LEAVE");
            simpMessagingTemplate.convertAndSend("/topic/game-progress/"+gameID,sendGameStatus);
        }
    }

    public void connectRandom(String player2){

    }

    //Hoàn thành xong game, lưu game vào lịch sử
    public void finishGame(){

    }

    public void handleGame(GameStatusDTO gameStatusDTO){
        String currentGameID = gameStatusDTO.getGameID();
        if(!GameStorage.getInstance().getGames().containsKey(currentGameID)){
            return;
        }
        else{
            simpMessagingTemplate.convertAndSend("/topic/game-progress/"+currentGameID,gameStatusDTO);
            if(gameStatusDTO.getMessage().equals("MOVE")){
                //Nếu message mà là bước đi thì lưu MOVE lại
                GameHistory history = GameStorage.getInstance().
                        getHistory().get(currentGameID);
                history.getHistory().add(gameStatusDTO);
                GameStorage.getInstance().registerHistory(history);
            }
        }
    }

    public void saveGame(){

    }
}
