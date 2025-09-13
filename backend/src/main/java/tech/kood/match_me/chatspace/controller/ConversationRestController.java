package tech.kood.match_me.chatspace.controller;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.match_me.chatspace.dto.ChatMessageDto;
import tech.kood.match_me.chatspace.dto.ConversationDto;
import tech.kood.match_me.chatspace.model.Conversation;
import tech.kood.match_me.chatspace.model.Message;
import tech.kood.match_me.chatspace.model.User;
import tech.kood.match_me.chatspace.service.ConversationService;
import tech.kood.match_me.chatspace.service.MessageService;

@RestController
@RequestMapping("/api/conversations")
public class ConversationRestController {

    private final ConversationService conversationService;
    private final MessageService messageService;

    public ConversationRestController(ConversationService conversationService, MessageService messageService) {
        this.conversationService = conversationService;
        this.messageService = messageService;
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

    @GetMapping("/user/{userId}")
    public List<ConversationDto> getUserConversations(@PathVariable UUID userId) {
        return conversationService.getUserConversationsSorted(userId).stream()
                .map(convo -> new ConversationDto(
                convo.getId(),
                convo.getParticipants().stream().map(User::getUsername).toList(),
                convo.getMessages().stream()
                        .sorted(Comparator.comparing(Message::getTimestamp))
                        .map(m -> new ChatMessageDto(
                        convo.getId(),
                        m.getSender().getId(),
                        m.getSender().getUsername(),
                        m.getContent(),
                        m.getTimestamp(),
                        "MESSAGE",
                        false
                ))
                        .toList()
        ))
                .toList();
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markConversationRead(
            @PathVariable UUID id,
            @RequestParam UUID userId
    ) {
        messageService.markConversationAsRead(id, userId);
        return ResponseEntity.ok().build();
    }
}
