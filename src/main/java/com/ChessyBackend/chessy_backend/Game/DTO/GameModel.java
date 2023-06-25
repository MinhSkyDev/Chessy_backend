package com.ChessyBackend.chessy_backend.Game.DTO;


import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameModel {
    String gameId;
    String player1;
    String player2;
    String status;
    String roomName;
    String secsPerMoves;
    String timeAllowStop;
}
