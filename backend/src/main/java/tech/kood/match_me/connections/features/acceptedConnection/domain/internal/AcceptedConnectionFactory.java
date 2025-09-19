package tech.kood.match_me.connections.features.acceptedConnection.domain.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.domain.internal.userId.UserId;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionId;

import java.time.Instant;

@Component
@Factory
@DomainLayer
public final class AcceptedConnectionFactory {

    private final Validator validator;

    public AcceptedConnectionFactory(Validator validator) {
        this.validator = validator;
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

    public AcceptedConnection createNew(ConnectionId connectionId, UserId acceptedByUserId, UserId acceptedUserId)
            throws CheckedConstraintViolationException {
        return create(connectionId, acceptedByUserId, acceptedUserId,
                Instant.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
    }
}
