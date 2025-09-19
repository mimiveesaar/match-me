package tech.kood.match_me.connections.features.rejectedConnection.domain.internal;


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
public final class RejectedConnectionFactory {

    private final Validator validator;

    private final ConnectionIdFactory connectionIdFactory;


    public RejectedConnectionFactory(Validator validator, ConnectionIdFactory connectionIdFactory) {
        this.validator = validator;
        this.connectionIdFactory = connectionIdFactory;
    }

    public RejectedConnection create(ConnectionId connectionId, UserId rejectedByUser, UserId rejectedUser, RejectedConnectionReason reason, Instant createdAt) throws CheckedConstraintViolationException {
        var rejectedConnection = new RejectedConnection(connectionId, rejectedByUser, rejectedUser, reason, createdAt);
        var validationResult = validator.validate(rejectedConnection);

        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return rejectedConnection;
    }

    public RejectedConnection createNew(UserId rejectedByUser, UserId rejectedUser, RejectedConnectionReason reason) throws CheckedConstraintViolationException {
        var connectionId = connectionIdFactory.createNew();
        return create(connectionId, rejectedByUser, rejectedUser, reason, Instant.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
    }
}
