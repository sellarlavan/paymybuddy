package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.model.UserConnections;
import com.paymybuddy.paymybuddy.service.UserConnectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/connections")
@RequiredArgsConstructor
public class UserConnectionsController {

    private final UserConnectionsService userConnectionsService;

    @PostMapping("/{userId}/contacts")
    public ResponseEntity<UserConnections> addContact(
            @PathVariable Long userId,
            @RequestParam String contactEmail
    ) {
        UserConnections connection = userConnectionsService.addContactByEmail(userId, contactEmail);
        return ResponseEntity.ok(connection);
    }
}
