package tech.kood.match_me.chatspace.service;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import tech.kood.match_me.chatspace.dto.UserConnectionDto;
import tech.kood.match_me.chatspace.model.Conversation;
import tech.kood.match_me.chatspace.model.UserConnection;
import tech.kood.match_me.chatspace.repository.ConversationRepository;
import tech.kood.match_me.chatspace.repository.UserConnectionRepository;

@Service
public class UserConnectionService {

    private final UserConnectionRepository userConnectionRepository;
    private final ConversationRepository conversationRepository;

    public UserConnectionService(UserConnectionRepository userConnectionRepository,
            ConversationRepository conversationRepository) {
        this.userConnectionRepository = userConnectionRepository;
        this.conversationRepository = conversationRepository;
    }

    public List<UserConnectionDto> getUserConnections(UUID userId) {
        List<UserConnection> connections = userConnectionRepository.findByUserIdWithConnectedUser(userId);

        return connections.stream()
                .map(conn -> {
                    // Get last conversation update with this connected user
                    UUID connectedUserId = conn.getConnectedUser().getId();
                    LocalDateTime lastUpdated = conversationRepository
                            .findByParticipantsIds(userId, connectedUserId)
                            .map(Conversation::getLastUpdatedAt)
                            .orElse(LocalDateTime.MIN);

                    UserConnectionDto dto = new UserConnectionDto(
                            connectedUserId,
                            conn.getConnectedUser().getUsername(),
                            conn.getConnectedUser().getStatus().name() // ✅ enum -> String
                    );

                    return new AbstractMap.SimpleEntry<>(lastUpdated, dto);
                })
                .sorted((a, b) -> b.getKey().compareTo(a.getKey())) // sort descending by lastUpdated
                .map(Map.Entry::getValue)
                .toList();
    }
}
