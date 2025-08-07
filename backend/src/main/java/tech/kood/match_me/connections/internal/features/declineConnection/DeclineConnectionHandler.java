package tech.kood.match_me.connections.internal.features.declineConnection;

import org.springframework.context.ApplicationEventPublisher;
import tech.kood.match_me.connections.internal.database.repositories.PendingConnectionsRepository;
import tech.kood.match_me.connections.internal.database.repositories.RejectedConnectionsRepository;

public final class DeclineConnectionHandler {

    private final RejectedConnectionsRepository rejectedConnectionsRepository;
    private final PendingConnectionsRepository pendingConnectionsRepository;
    private final ApplicationEventPublisher events;

    public DeclineConnectionHandler(RejectedConnectionsRepository rejectedConnectionsRepository,
            PendingConnectionsRepository pendingConnectionsRepository,
            ApplicationEventPublisher events) {
        this.rejectedConnectionsRepository = rejectedConnectionsRepository;
        this.pendingConnectionsRepository = pendingConnectionsRepository;
        this.events = events;
    }

    public DeclineConnectionResults handle(DeclineConnectionRequest request) {
        if (request == null || request.connectionId() == null || request.requestId() == null) {
            var result = new DeclineConnectionResults.InvalidRequest(request.requestId(),
                    request.tracingId());
            events.publishEvent(new DeclineConnectionEvent(request, result));
            return result;
        }


    }


}
