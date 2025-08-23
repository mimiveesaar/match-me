package tech.kood.match_me.connections.features.pendingConnection.domain.internal;


import jakarta.validation.Validator;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionId;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionIdFactory;
import tech.kood.match_me.common.domain.internal.userId.UserId;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@Factory
public final class PendingConnectionFactory {

    private final Validator validator;

    private final ConnectionIdFactory connectionIdFactory;

    public PendingConnectionFactory(Validator validator, ConnectionIdFactory connectionIdFactory) {
        this.validator = validator;
        this.connectionIdFactory = connectionIdFactory;
    }

    public PendingConnection create(ConnectionId connectionId, UserId senderId, UserId targetId, Instant createdAt) throws CheckedConstraintViolationException {
        var pendingConnection = new PendingConnection(connectionId, senderId, targetId, createdAt);
        var validationResult = validator.validate(pendingConnection);

        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return pendingConnection;
    }

    public PendingConnection createNew(UserId senderId, UserId targetId) throws CheckedConstraintViolationException {
        var connectionId = connectionIdFactory.createNew();
        return create(connectionId, senderId, targetId, Instant.now().truncatedTo(ChronoUnit.SECONDS));
    }
}
