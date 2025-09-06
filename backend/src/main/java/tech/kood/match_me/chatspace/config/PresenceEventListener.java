package tech.kood.match_me.chatspace.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class PresenceEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public PresenceEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        // Update user status to ONLINE
        // Notify subscribers
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        // Update user status to OFFLINE
        // Notify subscribers
    }
}
