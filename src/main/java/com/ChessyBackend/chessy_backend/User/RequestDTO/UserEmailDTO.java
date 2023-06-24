package com.ChessyBackend.chessy_backend.User.RequestDTO;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserEmailDTO {
    String username;
    String email;
}
