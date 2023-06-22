package com.ChessyBackend.chessy_backend.User;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @GetMapping("/{username}")
    public void getUser(@PathVariable String username){

    }

}
