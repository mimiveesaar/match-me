package tech.kood.match_me.connections.features.connectionRequest;

import java.time.Instant;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import tech.kood.match_me.connections.internal.database.entities.PendingConnectionEntity;
import tech.kood.match_me.connections.internal.database.repositories.PendingConnectionsRepository;

@Component
public class ConnectionRequestHandler {


    private final PendingConnectionsRepository pendingConnectionsRepository;
    private final ApplicationEventPublisher events;

    public ConnectionRequestHandler(PendingConnectionsRepository pendingConnectionsRepository,
            ApplicationEventPublisher events) {
        this.pendingConnectionsRepository = pendingConnectionsRepository;
        this.events = events;
    }

    public ConnectionRequestResults handle(ConnectionRequest request) {

        if (request == null || request.requestId() == null) {
            throw new IllegalArgumentException("Request and requestId must not be null");
        }

        if (request.targetUserId() == null || request.senderId() == null) {
            var result = new ConnectionRequestResults.InvalidRequest(request.requestId(),
                    request.tracingId());
            events.publishEvent(new ConnectionRequestCreatedEvent(request, result));
            return result;
        }

        try {
            var pendingConnection = pendingConnectionsRepository.findBySenderAndTarget(
                    UUID.fromString(request.senderId()), UUID.fromString(request.targetUserId()));

            if (pendingConnection.isPresent()) {
                var result = new ConnectionRequestResults.AlreadyExists(request.requestId(),
                        request.tracingId());
                events.publishEvent(new ConnectionRequestCreatedEvent(request, result));
                return result;
            }

            var pendingConnectionEntity = new PendingConnectionEntity(UUID.randomUUID(),
                    UUID.fromString(request.senderId()), UUID.fromString(request.targetUserId()),
                    Instant.now());

            pendingConnectionsRepository.save(pendingConnectionEntity);

            var result = new ConnectionRequestResults.Success(request.requestId(),
                    pendingConnectionEntity.id().toString(), request.targetUserId(),
                    request.senderId(), request.tracingId());
            events.publishEvent(new ConnectionRequestCreatedEvent(request, result));
            return result;

        } catch (Exception e) {
            var result = new ConnectionRequestResults.SystemError(request.requestId(),
                    e.getMessage(), request.tracingId());
            events.publishEvent(new ConnectionRequestCreatedEvent(request, result));
            return result;
        }
    }
}
