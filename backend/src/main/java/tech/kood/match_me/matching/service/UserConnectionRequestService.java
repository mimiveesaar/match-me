package tech.kood.match_me.matching.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tech.kood.match_me.matching.events.UserConnectionRequestEvent;
import tech.kood.match_me.matching.model.ConnectionRequest;
import tech.kood.match_me.matching.repository.ConnectionRequestRepository;

import java.util.UUID;

@Service
public class UserConnectionRequestService {

    private final ConnectionRequestRepository connectionRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UserConnectionRequestService(ConnectionRequestRepository connectionRequestRepository,
                             ApplicationEventPublisher eventPublisher) {
        this.connectionRequestRepository = connectionRequestRepository;
        this.eventPublisher = eventPublisher;
    }

    public void sendConnectionRequest(UUID requesterId, UUID requestedId) {
        ConnectionRequest connectionRequest = new ConnectionRequest(requesterId, requestedId);
        connectionRequestRepository.save(connectionRequest);

        // Publish event
        eventPublisher.publishEvent(
                new UserConnectionRequestEvent(requesterId, requestedId)
        );
        System.out.println("UserConnectionRequestEvent sent: requesterId=" + requesterId +
                ", requestedId=" + requestedId);
    }
}
