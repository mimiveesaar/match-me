package tech.kood.match_me.chatspace.service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.chatspace.model.Conversation;
import tech.kood.match_me.chatspace.model.Message;
import tech.kood.match_me.chatspace.model.User;
import tech.kood.match_me.chatspace.repository.ChatUserRepository;
import tech.kood.match_me.chatspace.repository.ConversationRepository;

@Service
@Transactional
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ChatUserRepository userRepository;

    public ConversationService(ConversationRepository conversationRepository, ChatUserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    public Conversation getOrCreateConversation(UUID userId, UUID otherUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("User not found: " + otherUserId));

        // Use fetch-join query
        return conversationRepository.findByParticipantsWithData(user, otherUser)
                .orElseGet(() -> {
                    Conversation conversation = new Conversation();
                    conversation.setParticipants(Set.of(user, otherUser));
                    return conversationRepository.save(conversation);
                });
    }

    public List<Message> getMessages(UUID conversationId) {
        Conversation conversation = conversationRepository
                .findByIdWithParticipantsAndMessages(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found: " + conversationId));

        return conversation.getMessages().stream()
                .sorted(Comparator.comparing(Message::getTimestamp))
                .toList();
    }
}
