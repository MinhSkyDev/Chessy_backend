package com.ChessyBackend.chessy_backend.Game.DTO;


import lombok.*;

import java.util.ArrayList;

//Dữ liệu 1 trận đấu để lưu xuống Database

@Data
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class GameHistory {
    String gameID;
    String player1;
    String player2;
    @NonNull
    ArrayList<GameStatusDTO> history;
}
