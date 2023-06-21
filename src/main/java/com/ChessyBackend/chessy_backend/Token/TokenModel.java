package com.ChessyBackend.chessy_backend.Token;

import lombok.*;

@Data
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class TokenModel {
    String accessToken;
    String refreshToken;
    String message;
}
