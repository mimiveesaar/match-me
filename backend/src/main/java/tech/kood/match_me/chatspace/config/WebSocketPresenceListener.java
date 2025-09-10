package tech.kood.match_me.chatspace.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketPresenceListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ConcurrentHashMap<String, String> onlineUsers = new ConcurrentHashMap<>();
    // key: sessionId, value: userId

    public WebSocketPresenceListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();

        // Hardcode the logged-in user for testing
        String userId = "11111111-1111-1111-1111-111111111111"; // Henry's UUID

        onlineUsers.put(sessionId, userId);

        // broadcast online status
        messagingTemplate.convertAndSend("/topic/status/" + userId, "ONLINE");
        System.out.println("User " + userId + " connected. Online users: " + onlineUsers.values());
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String userId = onlineUsers.remove(sessionId);

        if (userId != null) {
            // broadcast that user is offline
            messagingTemplate.convertAndSend("/topic/status/" + userId, "OFFLINE");
            System.out.println("User " + userId + " disconnected. Online users: " + onlineUsers.values());
        }
    }
}
