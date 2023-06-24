package com.ChessyBackend.chessy_backend.Game.DTO;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameStatusDTO {
    String gameID;
    String player1;
    String player2;
    String FEN;
    String moves;
    String currentSide;

    String message;
}
