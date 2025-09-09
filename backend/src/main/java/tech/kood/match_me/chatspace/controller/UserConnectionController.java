package tech.kood.match_me.chatspace.controller;

import org.springframework.web.bind.annotation.*;
import tech.kood.match_me.chatspace.dto.UserConnectionDto;
import tech.kood.match_me.chatspace.service.UserConnectionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserConnectionController {

    private final UserConnectionService userConnectionService;

    public UserConnectionController(UserConnectionService userConnectionService) {
        this.userConnectionService = userConnectionService;
    }

    // GET /api/users/{userId}/connections
    @GetMapping("/{userId}/connections")
    public List<UserConnectionDto> getConnections(@PathVariable UUID userId) {
        return userConnectionService.getUserConnections(userId);
    }
}