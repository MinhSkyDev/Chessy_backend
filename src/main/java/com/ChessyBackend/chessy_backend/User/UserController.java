package com.ChessyBackend.chessy_backend.User;

import com.ChessyBackend.chessy_backend.User.RequestDTO.UserMessage;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin
public class UserController {

    @Autowired
    public UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<UserModel> getUser(@PathVariable String username){
        return new ResponseEntity<UserModel>(userService.getUser(username), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserMessage> updateAllAttributeUser(@RequestBody UserModel userModel){
        UserMessage userMessage = new UserMessage(userService.updateUser(userModel));

        return new ResponseEntity<UserMessage>(userMessage, HttpStatus.OK);
    }

    @PutMapping("/{attribute}")
    public ResponseEntity<UserMessage> updateUser(@RequestBody UserModel userModel, @PathVariable String attribute){
        UserMessage userMessage = new UserMessage(userService.updateUser(userModel,attribute));
        return new ResponseEntity<UserMessage>(userMessage, HttpStatus.OK);
    }



}
