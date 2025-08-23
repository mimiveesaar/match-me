package tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.InfrastructureLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionReason;

import java.time.Instant;
import java.util.UUID;

@Component
@Factory
@InfrastructureLayer
public class RejectedConnectionEntityFactory {

    private final Validator validator;

    public RejectedConnectionEntityFactory(Validator validator) {
        this.validator = validator;
    }

    public RejectedConnectionEntity create(UUID id, UUID rejectedByUserId, UUID rejectedUserId,
            RejectedConnectionReason reason, Instant createdAt)
            throws CheckedConstraintViolationException {
        var entity = new RejectedConnectionEntity(id, rejectedByUserId, rejectedUserId, reason,
                createdAt);
        var validationResult = validator.validate(entity);
        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return entity;
    }
}
