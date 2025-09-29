package tech.kood.match_me.chatspace.service;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import tech.kood.match_me.chatspace.dto.UserConnectionDto;
import tech.kood.match_me.chatspace.model.Conversation;
import tech.kood.match_me.chatspace.model.User;
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
                    // Get the OTHER user (not the current user)
                    User otherUser = conn.getUser().getId().equals(userId) 
                        ? conn.getConnectedUser() 
                        : conn.getUser();
                    
                    UUID otherUserId = otherUser.getId();

                    System.out.println("[UserConnectionService] User: " + otherUser.getUsername()
                            + ", profilePicSrc: " + otherUser.getProfilePicSrc());
                    
                    // Get last conversation update with this other user
                    LocalDateTime lastUpdated = conversationRepository
                            .findByParticipantsIds(userId, otherUserId)
                            .map(Conversation::getLastUpdatedAt)
                            .orElse(LocalDateTime.MIN);

                    UserConnectionDto dto = new UserConnectionDto(
                            otherUserId,
                            otherUser.getUsername(),
                            otherUser.getStatus().name(),
                            otherUser.getProfilePicSrc()
                    );

                    System.out.println("[UserConnectionService] Created DTO: " + dto);

                    return new AbstractMap.SimpleEntry<>(lastUpdated, dto);
                })
                .sorted((a, b) -> b.getKey().compareTo(a.getKey()))
                .map(Map.Entry::getValue)
                .toList();
    }
}