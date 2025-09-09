package tech.kood.match_me.chatspace.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import tech.kood.match_me.chatspace.dto.ChatMessageDTO;
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

    public MessageService(MessageRepository messageRepository,
            ChatUserRepository userRepository,
            ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
    }

    // Saving messages
    public ChatMessageDTO saveMessage(ChatMessageDTO dto) {

        UUID senderUuid = UUID.fromString(dto.getSenderId().toString());
        UUID conversationUuid = UUID.fromString(dto.getConversationId().toString());
        // Find sender by UUID
        User sender = userRepository.findById(senderUuid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find conversation by UUID
        Conversation conversation = conversationRepository.findById(conversationUuid)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // Save message to DB
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(dto.getContent());
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);

        Message saved = messageRepository.save(message);

        // Update conversation "lastUpdatedAt"
        conversation.setLastUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        // Update DTO with timestamp and type for broadcasting
        dto.setTimestamp(saved.getTimestamp());
        dto.setType("MESSAGE");

        return dto;
    }
}
