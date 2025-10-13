package tech.kood.match_me.matching.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tech.kood.match_me.matching.events.UserRejectionRequestEvent;
import tech.kood.match_me.matching.model.RejectionRequestEntity;
import tech.kood.match_me.matching.repository.RejectionRequestRepository;

import java.util.UUID;

@Service
public class UserRejectionRequestService {

    private final RejectionRequestRepository rejectionRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UserRejectionRequestService(RejectionRequestRepository rejectionRequestRepository,
                                       ApplicationEventPublisher eventPublisher) {
        this.rejectionRequestRepository = rejectionRequestRepository;
        this.eventPublisher = eventPublisher;
    }

    public void sendRejectionRequest(UUID requesterId, UUID requestedId) {
        RejectionRequestEntity rejectionRequest = new RejectionRequestEntity(requesterId, requestedId);
        rejectionRequestRepository.save(rejectionRequest);

        // Publish event
        eventPublisher.publishEvent(new UserRejectionRequestEvent(requesterId, requestedId));
        System.out.println("UserRejectionRequestEvent sent: requesterId=" + requesterId +
                ", requestedId=" + requestedId);
    }
}

