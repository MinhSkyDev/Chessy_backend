package com.ChessyBackend.chessy_backend.Game.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConnectRequest {
    String player;
    String gameId;
    String roomName;
    String secsPerMoves;
    String timeAllowStop;
}
