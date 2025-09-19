package tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.InfrastructureLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;

import java.time.Instant;
import java.util.UUID;

@InfrastructureLayer
@Component
public class AcceptedConnectionEntityFactory {


    private final Validator validator;

    public AcceptedConnectionEntityFactory(Validator validator) {
        this.validator = validator;
    }

    public AcceptedConnectionEntity create(UUID id, UUID acceptedByUserId, UUID acceptedUserId,
            Instant createdAt) throws CheckedConstraintViolationException {
        var acceptedConnectionEntity = new AcceptedConnectionEntity(id, acceptedByUserId, acceptedUserId, createdAt);
        var validationResult = validator.validate(acceptedConnectionEntity);
        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return acceptedConnectionEntity;
    }
}
