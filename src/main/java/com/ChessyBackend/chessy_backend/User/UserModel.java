package com.ChessyBackend.chessy_backend.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UserModel {
    String username;
    String password;
    String email;
    String name;
    String avatarURL;
}
