package tech.kood.match_me.chatspace.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.chatspace.dto.UserConnectionDto;
import tech.kood.match_me.chatspace.model.UserConnection;
import tech.kood.match_me.chatspace.repository.UserConnectionRepository;

@Service
public class UserConnectionService {

    private final UserConnectionRepository userConnectionRepository;

    public UserConnectionService(UserConnectionRepository userConnectionRepository) {
        this.userConnectionRepository = userConnectionRepository;
    }

    @Transactional(readOnly = true)
    public List<UserConnectionDto> getUserConnections(UUID userId) {
        List<UserConnection> connections = userConnectionRepository.findByUserId(userId);

        return connections.stream()
                .map(conn -> new UserConnectionDto(
                conn.getConnectedUser().getId(),
                conn.getConnectedUser().getUsername(),
                conn.getConnectedUser().getStatus().toString()
        ))
                .toList();
    }
}
