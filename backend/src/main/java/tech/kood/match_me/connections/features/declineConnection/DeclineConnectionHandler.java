package tech.kood.match_me.connections.features.declineConnection;

import org.springframework.context.ApplicationEventPublisher;
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
}
