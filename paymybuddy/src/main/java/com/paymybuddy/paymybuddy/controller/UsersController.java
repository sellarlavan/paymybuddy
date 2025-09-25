package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.model.Users;
import com.paymybuddy.paymybuddy.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users user){
        Users registeredUser = usersService.register(user);
        return ResponseEntity.ok(registeredUser);
    }
}
