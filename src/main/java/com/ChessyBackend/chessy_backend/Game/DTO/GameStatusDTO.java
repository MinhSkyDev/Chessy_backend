package com.ChessyBackend.chessy_backend.Game.DTO;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameStatusDTO {
    String gameID;
    String player1;
    String player2;
    String fen;
    String moves;
    String currentSide;
    String message;
}
