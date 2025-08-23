package tech.kood.match_me.connections.common.domain.connectionId;


import jakarta.validation.Validator;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;

import java.util.UUID;


@Factory
@Component
public final class ConnectionIdFactory {

    private final Validator validator;

    public ConnectionIdFactory(Validator validator) {
        this.validator = validator;
    }

    public ConnectionId create(UUID value) throws CheckedConstraintViolationException {
        var connectionId = new ConnectionId(value);
        var validationResult = validator.validate(connectionId);

        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return connectionId;
    }

    public ConnectionId from(ConnectionId value) throws CheckedConstraintViolationException {
        return create(value.getValue());
    }

    public ConnectionId createNew() {
        return new ConnectionId(UUID.randomUUID());
    }

}
