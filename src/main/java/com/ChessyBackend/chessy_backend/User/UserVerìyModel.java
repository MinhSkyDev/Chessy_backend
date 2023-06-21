package com.ChessyBackend.chessy_backend.User;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserVerìyModel {
    String username;
    Boolean isVerify;
    String otp;
}
