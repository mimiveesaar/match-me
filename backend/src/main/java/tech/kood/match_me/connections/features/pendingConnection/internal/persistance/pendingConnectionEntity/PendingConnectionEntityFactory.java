package tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity;


import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.InfrastructureLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;

import java.time.Instant;
import java.util.UUID;


@Component
@Factory
@InfrastructureLayer
public class PendingConnectionEntityFactory {

    private final Validator validator;

    public PendingConnectionEntityFactory(Validator validator) {
        this.validator = validator;
    }

    public PendingConnectionEntity create(UUID id, UUID senderId, UUID targetId, Instant createdAt) throws CheckedConstraintViolationException {
        var entity = new PendingConnectionEntity(id, senderId, targetId, createdAt);
        var validationResult = validator.validate(entity);
        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return entity;
    }
}
