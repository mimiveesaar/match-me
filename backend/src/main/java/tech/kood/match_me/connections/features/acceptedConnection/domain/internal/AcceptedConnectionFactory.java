package tech.kood.match_me.connections.features.acceptedConnection.domain.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.domain.internal.userId.UserId;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionId;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionIdFactory;

import java.time.Instant;

@Component
@Factory
@DomainLayer
public final class AcceptedConnectionFactory {

    private final Validator validator;
    private final ConnectionIdFactory connectionIdFactory;

    public AcceptedConnectionFactory(Validator validator, ConnectionIdFactory connectionIdFactory) {
        this.validator = validator;
        this.connectionIdFactory = connectionIdFactory;
    }

    public AcceptedConnection create(ConnectionId connectionId, UserId acceptedByUser,
            UserId acceptedUser, Instant createdAt) throws CheckedConstraintViolationException {
        var acceptedConnection =
                new AcceptedConnection(connectionId, acceptedByUser, acceptedUser, createdAt);
        var validationResult = validator.validate(acceptedConnection);

        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return acceptedConnection;
    }

    public AcceptedConnection createNew(UserId acceptedByUserId, UserId acceptedUserId)
            throws CheckedConstraintViolationException {
        var connectionId = connectionIdFactory.createNew();
        return create(connectionId, acceptedByUserId, acceptedUserId,
                Instant.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
    }
}
