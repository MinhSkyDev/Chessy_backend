package com.ChessyBackend.chessy_backend.Game;


import com.ChessyBackend.chessy_backend.Game.DTO.ConnectRequest;
import com.ChessyBackend.chessy_backend.Game.DTO.GameModel;
import com.ChessyBackend.chessy_backend.Game.DTO.GameStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/game")
public class GameController {


    @Autowired
    public GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameModel> createGame(@RequestBody ConnectRequest connectRequest){
        GameModel gameModel = gameService.createGame(connectRequest.getPlayer());
        return new ResponseEntity<GameModel>(gameModel,HttpStatus.OK);
    }

    @PostMapping("/connect")
    public ResponseEntity<GameModel> connectSpecificGame(@RequestBody ConnectRequest connectRequest){
        GameModel gameModel = gameService.connectGame(connectRequest.getPlayer(),connectRequest.getGameId());

        return new ResponseEntity<GameModel>(gameModel,HttpStatus.OK);

    }

    @PostMapping("/connect/{id}")
    public void connectRandomGame(@PathVariable String gameId){}

    @GetMapping("/rooms")
    public void returnRooms(){

    }

    @PostMapping("/gameplay")
    public ResponseEntity gameplay(@RequestBody GameStatusDTO gameStatusDTO){
        gameService.handleGame(gameStatusDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    public void deleteGame(){

    }

}
