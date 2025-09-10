package tech.kood.match_me.chatspace.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChatMessageDTO {

    private UUID conversationId;
    private UUID senderId;
    private String senderUsername; // NEW: helpful for frontend display
    private String content;
    private LocalDateTime timestamp;
    private String type; // MESSAGE, TYPING, PRESENCE
    private Boolean typing;

    public ChatMessageDTO(UUID conversationId,
            UUID senderId,
            String senderUsername,
            String content,
            LocalDateTime timestamp,
            String type,
            Boolean typing) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.timestamp = timestamp;
        this.type = type;
        this.typing = typing;
    }

    // Getters and setters
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

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getTyping() {
        return typing;
    }

    public void setTyping(Boolean typing) {
        this.typing = typing;
    }
}
