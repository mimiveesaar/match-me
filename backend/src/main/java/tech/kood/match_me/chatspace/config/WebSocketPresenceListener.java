package tech.kood.match_me.chatspace.config;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import tech.kood.match_me.chatspace.model.User;
import tech.kood.match_me.chatspace.model.UserStatus;
import tech.kood.match_me.chatspace.repository.ChatUserRepository;

@Component
public class WebSocketPresenceListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatUserRepository chatUserRepository;
    private final ConcurrentHashMap<String, String> onlineUsers = new ConcurrentHashMap<>();

    public WebSocketPresenceListener(SimpMessagingTemplate messagingTemplate, ChatUserRepository chatUserRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatUserRepository = chatUserRepository;
    }

    public Set<String> getOnlineUserIds() {
        return new HashSet<>(onlineUsers.values());
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        System.out.println("[CONNECT] Full headers:");
        accessor.toNativeHeaderMap().forEach((key, value) -> {
            System.out.println("  " + key + ": " + value);
        });

        String userId = accessor.getFirstNativeHeader("userId"); // <-- passed from frontend
        System.out.println("[CONNECT] sessionId=" + sessionId + ", userId=" + userId);

        if (userId == null) {
            System.out.println("Missing userId in connection headers");
            return;
        }

        onlineUsers.put(sessionId, userId);

        // Update DB
        try {
            User user = chatUserRepository.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            user.setStatus(UserStatus.ONLINE);
            user.setLastActive(LocalDateTime.now());
            chatUserRepository.save(user);
            System.out.println("[CONNECT] DB updated: user " + user.getUsername() + " is ONLINE");
        } catch (Exception e) {
            System.err.println("[CONNECT] Failed to update DB for userId=" + userId + ": " + e.getMessage());
            e.printStackTrace();
        }

        // Broadcast online status to common topic
        messagingTemplate.convertAndSend("/topic/status", Map.of(
                "userId", userId,
                "status", "ONLINE"
        ));
        System.out.println("[CONNECT] Broadcast ONLINE for userId=" + userId);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String userId = onlineUsers.remove(sessionId);

        System.out.println("[DISCONNECT] sessionId=" + sessionId + ", userId=" + userId);

        if (userId != null) {
            boolean stillOnline = onlineUsers.containsValue(userId);

            if (!stillOnline) {
                try {
                    User user = chatUserRepository.findById(UUID.fromString(userId))
                            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
                    user.setStatus(UserStatus.OFFLINE);
                    user.setLastActive(LocalDateTime.now());
                    chatUserRepository.save(user);
                    System.out.println("[DISCONNECT] DB updated: user " + user.getUsername() + " is OFFLINE");
                } catch (Exception e) {
                    System.err.println("[DISCONNECT] Failed to update DB for userId=" + userId + ": " + e.getMessage());
                    e.printStackTrace();
                }

                messagingTemplate.convertAndSend("/topic/status", Map.of(
                        "userId", userId,
                        "status", "OFFLINE"
                ));
                System.out.println("[DISCONNECT] Broadcast OFFLINE for userId=" + userId);
            } else {
                System.out.println("[DISCONNECT] User " + userId + " still has active sessions. Skipping offline update.");
            }
        }
    }
}
