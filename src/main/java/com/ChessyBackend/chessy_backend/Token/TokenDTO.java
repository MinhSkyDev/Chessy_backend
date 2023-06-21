package com.ChessyBackend.chessy_backend.Token;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class TokenDTO {
    String username;
    String access_token;
    String refresh_token;
}
