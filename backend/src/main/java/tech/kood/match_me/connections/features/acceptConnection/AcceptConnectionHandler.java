package tech.kood.match_me.connections.features.acceptConnection;

import java.time.Instant;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import jakarta.transaction.Transactional;
import tech.kood.match_me.connections.internal.database.entities.AcceptedConnectionEntity;
import tech.kood.match_me.connections.internal.database.repositories.AcceptedConnectionsRepository;

public final class AcceptConnectionHandler {


    private final AcceptedConnectionsRepository acceptedConnectionsRepository;

    private final PendingConnectionsRepository pendingConnectionsRepository;

    private final ApplicationEventPublisher events;

    public AcceptConnectionHandler(AcceptedConnectionsRepository acceptedConnectionsRepository,
            PendingConnectionsRepository pendingConnectionsRepository,
            ApplicationEventPublisher events) {
        this.acceptedConnectionsRepository = acceptedConnectionsRepository;
        this.pendingConnectionsRepository = pendingConnectionsRepository;
        this.events = events;
    }

    @Transactional
    public AcceptConnectionResults handle(AcceptConnectionRequest request) {


        if (request.connectionRequestId() == null || request.requestId() == null) {
            var result = new AcceptConnectionResults.DoesNotExist(request.requestId(),
                    request.connectionRequestId(), request.tracingId());

            events.publishEvent(new AcceptConnectionEvent(request, result));
            return result;
        }

        try {
            var connectionRequest = pendingConnectionsRepository
                    .findById(UUID.fromString(request.connectionRequestId()));

            if (connectionRequest.isEmpty()) {
                var result = new AcceptConnectionResults.DoesNotExist(request.requestId(),
                        request.connectionRequestId(), request.tracingId());
                events.publishEvent(new AcceptConnectionEvent(request, result));
                return result;
            }

            var pendingConnection = connectionRequest.get();

            // Make sure the connection is not already accepted. Multiple users might try to accept
            // at the same time. Handling should be serialized.
            if (acceptedConnectionsRepository.existsBetweenUsers(pendingConnection.senderUserId(),
                    pendingConnection.targetUserId())) {
                var result = new AcceptConnectionResults.AlreadyExists(request.requestId(),
                        request.connectionRequestId(), request.tracingId());
                events.publishEvent(new AcceptConnectionEvent(request, result));
                return result;
            }

            var acceptedConnection = new AcceptedConnectionEntity(pendingConnection.id(),
                    pendingConnection.senderUserId(), pendingConnection.targetUserId(),
                    Instant.now());

            acceptedConnectionsRepository.save(acceptedConnection);

            var result = new AcceptConnectionResults.Success(request.requestId(),
                    request.connectionRequestId(), request.tracingId());
            events.publishEvent(new AcceptConnectionEvent(request, result));
            return result;

        } catch (Exception e) {
            var result = new AcceptConnectionResults.SystemError(request.requestId(),
                    request.connectionRequestId(), request.tracingId(), e.getMessage());
            events.publishEvent(new AcceptConnectionEvent(request, result));
            return result;
        }
    }
}
