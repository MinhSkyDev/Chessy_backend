package com.ChessyBackend.chessy_backend.Email.DTO;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OtpDTO {
    String username;
    String email;
    Boolean isVerify;
    String otp;

}
