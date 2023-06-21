package com.ChessyBackend.chessy_backend.Authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class AuthenticateDTO {
    String username;
    String password;
}
