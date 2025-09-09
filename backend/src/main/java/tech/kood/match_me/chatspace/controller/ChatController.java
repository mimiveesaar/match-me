package tech.kood.match_me.chatspace.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import tech.kood.match_me.chatspace.dto.ChatMessageDTO;
import tech.kood.match_me.chatspace.service.MessageService;

@Controller
public class ChatController {

    private final MessageService messageService;

    public ChatController(MessageService messageService) {
        this.messageService = messageService;
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
