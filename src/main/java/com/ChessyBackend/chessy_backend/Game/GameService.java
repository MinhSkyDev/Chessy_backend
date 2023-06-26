package com.ChessyBackend.chessy_backend.Game;


import com.ChessyBackend.chessy_backend.Game.DTO.ConnectRequest;
import com.ChessyBackend.chessy_backend.Game.DTO.GameHistory;
import com.ChessyBackend.chessy_backend.Game.DTO.GameModel;
import com.ChessyBackend.chessy_backend.Game.DTO.GameStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private GameRepository gameRepository;

    //Tạo game với usertạo
    public GameModel createGame(ConnectRequest connectRequest){
        GameModel newGame = new GameModel();
        newGame.setPlayer1(connectRequest.getPlayer());
        newGame.setStatus("OPEN");
        newGame.setGameId(UUID.randomUUID().toString());
        newGame.setRoomName(connectRequest.getRoomName());
        newGame.setSecsPerMoves(connectRequest.getSecsPerMoves());
        newGame.setTimeAllowStop(connectRequest.getTimeAllowStop());

        //Đăng ký game mới với lại instance của GameStorage
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
                    gameModel.getPlayer2(),"NULL","NULL","Black","SYSTEM CONNECT");

            //Thông báo info của đối thủ qua cho mọi người
            simpMessagingTemplate.convertAndSend("/topic/game-progress/"+gameID,sendGameStatus);
            return gameModel;
        }
    }

    //Player2 leaveRoom
    public String leaveRoom(String player2, String gameID){
        if(!GameStorage.getInstance().getGames().containsKey(gameID)){
            return "INVALID";
        }
        else{
            GameModel gameModel = GameStorage.getInstance().getGames().get(gameID);
            gameModel.setPlayer2("NULL");
            gameModel.setStatus("OPEN");
            GameStorage.getInstance().registerNewGame(gameModel);

            GameStatusDTO sendGameStatus = new GameStatusDTO(gameID, gameModel.getPlayer1(),
                    gameModel.getPlayer2(),"NULL","NULL","Black","SYSTEM LEAVE");
            simpMessagingTemplate.convertAndSend("/topic/game-progress/"+gameID,sendGameStatus);
            return "LEAVE SUCCESS";
        }
    }

    public GameModel connectRandom(String player2){
        ArrayList<GameModel> currentGames = new ArrayList<>(GameStorage.getInstance().getGames().values());
        currentGames.removeIf(game -> !game.getStatus().equals("OPEN"));

        if(!currentGames.isEmpty()) {
            Collections.shuffle(currentGames);
            GameModel randomGame = currentGames.get(0);

            //Set các thông số cho game lấy ra
            randomGame.setPlayer2(player2);
            randomGame.setStatus("INPROGRESS");

            //Save vào Storage
            GameStorage.getInstance().registerNewGame(randomGame);

            //Gửi tới tất cả các người chơi subscribe vào topic (cụ thể là tới player1)
            GameStatusDTO sendGameStatus = new GameStatusDTO(randomGame.getGameId(), randomGame.getPlayer1(),
                    randomGame.getPlayer2(),"NULL","NULL","Black","SYSTEM CONNECT");
            return randomGame;
        }
        else{
            return null;
        }
    }

    public String startGame(String gameID){
        if(!GameStorage.getInstance().getGames().containsKey(gameID)){
            return "INVALID";
        }
        else{
            GameModel gameModel = GameStorage.getInstance().getGames().get(gameID);

            //Set thông số
            gameModel.setStatus("PLAYING");

            //Save vào Storage
            GameStorage.getInstance().registerNewGame(gameModel);


            //Gửi tới các người chơi khác
            GameStatusDTO sendGameStatus = new GameStatusDTO(gameID, gameModel.getPlayer1(),
                    gameModel.getPlayer2(),"NULL","NULL","Black","SYSTEM START");
            simpMessagingTemplate.convertAndSend("/topic/game-progress/"+gameID,sendGameStatus);
            return "VALID";
        }
    }


    //Hoàn thành xong game, lưu game vào lịch sử
    public String finishGame(String gameID){
        if(!GameStorage.getInstance().getGames().containsKey(gameID)){
            return "INVALID";
        }
        else{
            GameModel gameModel = GameStorage.getInstance().getGames().get(gameID);

            //Set thông số
            gameModel.setStatus("FINISHED");

            //Save vào Storage
            //Save trước để không bị nhập nhằng lúc lưu xuống database
            GameStorage.getInstance().registerNewGame(gameModel);

            //Save vào trong database
            GameHistory gameHistory = GameStorage.getInstance().getHistory().get(gameID);
            System.out.print(gameHistory);
            gameRepository.saveHistory(gameHistory);
            GameStorage.getInstance().finishGame(gameID);


            //Gửi tới các người chơi khác
            GameStatusDTO sendGameStatus = new GameStatusDTO(gameID, gameModel.getPlayer1(),
                    gameModel.getPlayer2(),"NULL","NULL","Black","SYSTEM FINISH");
            simpMessagingTemplate.convertAndSend("/topic/game-progress/"+gameID,sendGameStatus);
            return "FINISH GAME";
        }
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

                //Set các thông số cho history
                if(history == null){
                    history = new GameHistory();
                    history.setHistory(new ArrayList<GameStatusDTO>());
                }
                history.setGameID(gameStatusDTO.getGameID());
                history.setPlayer1(gameStatusDTO.getPlayer1());
                history.setPlayer2(gameStatusDTO.getPlayer2());

                history.getHistory().add(gameStatusDTO);
                GameStorage.getInstance().registerHistory(history);
                System.out.println(history);
            }
        }
    }

    public ArrayList<GameModel> getRooms() {
        ArrayList<GameModel> currentGames = new ArrayList<>(GameStorage.getInstance().getGames().values());
        currentGames.removeIf(game -> !game.getStatus().equals("OPEN"));

        return currentGames;
    }

    public ArrayList<GameHistory> getHistory(String username) {
        ArrayList<GameHistory> history = gameRepository.getHistory(username);
        return history;
    }
}
