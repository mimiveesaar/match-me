package tech.kood.match_me.chatspace.controller;

import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import tech.kood.match_me.chatspace.dto.ChatMessageDto;
import tech.kood.match_me.chatspace.service.MessageService;


@Controller
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    // Send a message to a specific conversation
    @MessageMapping("/chat/{conversationId}/sendMessage")
    public void sendMessage(
            @DestinationVariable UUID conversationId,
            @Payload ChatMessageDto message) {

        ChatMessageDto savedMessage = messageService.saveMessage(message);

        // broadcast to only that conversation
        messagingTemplate.convertAndSend(
                "/topic/conversations/" + conversationId,
                savedMessage
        );
    }

    // Typing indicator
    @MessageMapping("/chat/{conversationId}/typing")
    public void typing(
            @DestinationVariable UUID conversationId,
            @Payload ChatMessageDto typingMessage) {

        typingMessage.setType("TYPING");

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + conversationId + "/typing",
                typingMessage
        );
    }
}
