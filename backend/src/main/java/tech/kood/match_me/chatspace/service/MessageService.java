package tech.kood.match_me.chatspace.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.chatspace.dto.ChatMessageDto;
import tech.kood.match_me.chatspace.model.Conversation;
import tech.kood.match_me.chatspace.model.Message;
import tech.kood.match_me.chatspace.model.MessageStatus;
import tech.kood.match_me.chatspace.model.User;
import tech.kood.match_me.chatspace.repository.ChatUserRepository;
import tech.kood.match_me.chatspace.repository.ConversationRepository;
import tech.kood.match_me.chatspace.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatUserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessageService(MessageRepository messageRepository,
            ChatUserRepository userRepository,
            ConversationRepository conversationRepository,
            SimpMessagingTemplate simpMessagingTemplate) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    // Saving messages
    public ChatMessageDto saveMessage(ChatMessageDto dto) {

        UUID senderUuid = UUID.fromString(dto.getSenderId().toString());
        UUID conversationUuid = UUID.fromString(dto.getConversationId().toString());
        // Find sender by UUID
        User sender = userRepository.findById(senderUuid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find conversation by UUID
        Conversation conversation = conversationRepository.findByIdWithParticipants(conversationUuid)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // Get all recipients (all participants except the sender)
        List<User> recipients = conversation.getParticipants().stream()
                .filter(u -> !u.getId().equals(sender.getId()))
                .toList();

        // Save message to DB
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(dto.getContent());
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);

        Message saved = messageRepository.save(message);

        System.out.println("🟢 Saved message ID: " + saved.getId() + ", content: " + saved.getContent());

        // Update conversation lastUpdatedAt
        conversation.setLastUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        // Broadcast unread to all recipients via STOMP
        for (User recipient : recipients) {
            boolean isUnread = true;

            Map<String, Object> payload = new HashMap<>();
            payload.put("hasUnread", isUnread);
            payload.put("conversationId", conversation.getId());

            simpMessagingTemplate.convertAndSend(
                    "/topic/unread/" + recipient.getId(),
                    payload
            );
        }

        // Update DTO with timestamp and type for broadcasting
        dto.setTimestamp(saved.getTimestamp());
        dto.setType("MESSAGE");
        dto.setSenderUsername(sender.getUsername());

        return dto;
    }

    // Marking messages as read
    @Transactional
    public void markConversationAsRead(UUID conversationId, UUID userId) {
        List<Message> unreadMessages = messageRepository.findByConversationIdAndSenderIdNotAndStatus(
                conversationId, userId, MessageStatus.SENT
        );

        for (Message msg : unreadMessages) {
            msg.setStatus(MessageStatus.READ);
        }

        messageRepository.saveAll(unreadMessages);

        simpMessagingTemplate.convertAndSend(
                "/topic/unread/" + userId,
                false
        );
    }

}
