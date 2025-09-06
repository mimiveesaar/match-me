package tech.kood.match_me.chatspace.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChatMessageDTO {
    private UUID conversationId;
    private UUID senderId;
    private String content;
    private LocalDateTime timestamp;
    private String type; // MESSAGE, TYPING, PRESENCE

    // Getters and Setters
    public UUID getConversationId() {
        return conversationId;
    }
    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }
    public UUID getSenderId() {
        return senderId;
    }
    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
