package com.ChessyBackend.chessy_backend.Email.DTO;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerifyDTO {
    String username;
    String email;
    String otp;
}
