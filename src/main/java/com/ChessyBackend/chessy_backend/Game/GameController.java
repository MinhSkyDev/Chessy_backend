package com.ChessyBackend.chessy_backend.Game;


import com.ChessyBackend.chessy_backend.Game.DTO.ConnectRequest;
import com.ChessyBackend.chessy_backend.Game.DTO.GameModel;
import com.ChessyBackend.chessy_backend.Game.DTO.GameStatusDTO;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/game")
public class GameController {


    @Autowired
    public GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameModel> createGame(@RequestBody ConnectRequest connectRequest){
        GameModel gameModel = gameService.createGame(connectRequest);
        return new ResponseEntity<GameModel>(gameModel,HttpStatus.OK);
    }

    @PostMapping("/connect")
    public ResponseEntity<GameModel> connectSpecificGame(@RequestBody ConnectRequest connectRequest){
        GameModel gameModel = gameService.connectGame(connectRequest.getPlayer(),connectRequest.getGameId());
        if(gameModel == null){
            return new ResponseEntity<GameModel>(gameModel,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<GameModel>(gameModel,HttpStatus.OK);

    }

    @PostMapping("/connect/random")
    public ResponseEntity<GameModel> connectRandomGame(@RequestBody ConnectRequest connectRequest){
        GameModel gameModel = gameService.connectRandom(connectRequest.getPlayer());
        if(gameModel == null){
            return new ResponseEntity<GameModel>(gameModel,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<GameModel>(gameModel,HttpStatus.OK);
    }

    @PostMapping("/start")
    public ResponseEntity<String> startGame(@RequestBody ConnectRequest connectRequest){
        return new ResponseEntity<String>(gameService.startGame(connectRequest.getGameId()), HttpStatus.OK);
    }

    @PostMapping("/leave")
    public ResponseEntity<String> leaveGame(@RequestBody ConnectRequest connectRequest){
        return  new ResponseEntity<String>(gameService.leaveRoom(connectRequest.getPlayer(),connectRequest.getGameId()), HttpStatus.OK);
    }

    @PostMapping("/finish")
    public ResponseEntity<String> finishGame(@RequestBody ConnectRequest connectRequest){
        return new ResponseEntity<String>(gameService.finishGame(connectRequest.getGameId()), HttpStatus.OK);
    }

    @GetMapping("/rooms")
    public ResponseEntity<ArrayList<GameModel>> returnCurrrentRooms(){
        ArrayList<GameModel> currentGames = gameService.getRooms();
        return (currentGames.size() != 0) ?
                new ResponseEntity<ArrayList<GameModel>>(currentGames,HttpStatus.OK) :
                new ResponseEntity<ArrayList<GameModel>>(currentGames,HttpStatus.NOT_FOUND);
    }

    @PostMapping("/gameplay")
    public ResponseEntity gameplay(@RequestBody GameStatusDTO gameStatusDTO){
        gameService.handleGame(gameStatusDTO);
        return new ResponseEntity(HttpStatus.OK);
    }



}
