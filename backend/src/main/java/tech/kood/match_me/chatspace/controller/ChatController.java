package tech.kood.match_me.chatspace.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import tech.kood.match_me.chatspace.dto.ChatMessageDTO;
import tech.kood.match_me.chatspace.service.MessageService;

@Controller
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        return messageService.saveMessage(message);
    }

    @MessageMapping("/chat.typing")
    @SendTo("/topic/typing")
    public ChatMessageDTO typing(ChatMessageDTO message) {
    System.out.println("Received typing event from " + message.getSenderId());
    return message;
}
}