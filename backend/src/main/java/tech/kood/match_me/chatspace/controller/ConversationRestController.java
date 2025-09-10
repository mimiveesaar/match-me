package tech.kood.match_me.chatspace.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.match_me.chatspace.dto.ChatMessageDto;
import tech.kood.match_me.chatspace.dto.ConversationDto;
import tech.kood.match_me.chatspace.model.Conversation;
import tech.kood.match_me.chatspace.model.User;
import tech.kood.match_me.chatspace.service.ConversationService;

@RestController
@RequestMapping("/api/conversations")
public class ConversationRestController {

    private final ConversationService conversationService;

    public ConversationRestController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    // Called when user clicks on a connection
    @PostMapping("/open")
    public ConversationDto openConversation(@RequestParam UUID userId, @RequestParam UUID otherUserId) {
        Conversation conversation = conversationService.getOrCreateConversation(userId, otherUserId);

        return new ConversationDto(
                conversation.getId(),
                conversation.getParticipants().stream().map(User::getUsername).toList(),
                conversationService.getMessages(conversation.getId())
                        .stream()
                        .map(m -> new ChatMessageDto(
                        conversation.getId(),
                        m.getSender().getId(),
                        m.getSender().getUsername(),
                        m.getContent(),
                        m.getTimestamp(),
                        "MESSAGE",
                        false
                ))
                        .toList()
        );
    }
}
