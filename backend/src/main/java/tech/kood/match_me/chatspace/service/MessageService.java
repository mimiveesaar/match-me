package tech.kood.match_me.chatspace.service;

import java.time.LocalDateTime;

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

    public ChatMessageDTO saveMessage(ChatMessageDTO dto) {
        // Find sender by UUID
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find conversation by UUID
        Conversation conversation = conversationRepository.findById(dto.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // Save message to DB
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(dto.getContent());
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);

        Message saved = messageRepository.save(message);

        // Update DTO with timestamp and type for broadcasting
        dto.setTimestamp(saved.getTimestamp());
        dto.setType("MESSAGE");

        return dto;
    }
}
