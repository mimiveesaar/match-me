package tech.kood.match_me.chatspace.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/presence")
public class PresenceController {

    private final WebSocketPresenceListener presenceListener;

    public PresenceController(WebSocketPresenceListener presenceListener) {
        this.presenceListener = presenceListener;
    }

    @GetMapping("/online-users")
    public Set<String> getOnlineUsers() {
        return new HashSet<>(presenceListener.getOnlineUserIds());
    }
}
