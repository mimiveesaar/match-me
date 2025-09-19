package tech.kood.match_me.connections.features.acceptedConnection;

import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity.AcceptedConnectionEntity;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity.AcceptedConnectionEntityFactory;

import java.time.Instant;
import java.util.UUID;

@Component
public class AcceptedConnectionEntityMother {

    private final AcceptedConnectionEntityFactory acceptedConnectionEntityFactory;

    public AcceptedConnectionEntityMother(AcceptedConnectionEntityFactory acceptedConnectionEntityFactory) {
        this.acceptedConnectionEntityFactory = acceptedConnectionEntityFactory;
    }

    public AcceptedConnectionEntity createAcceptedConnectionEntity() {
        try {
            return acceptedConnectionEntityFactory.create(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    Instant.now()
            );
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    public AcceptedConnectionEntity withSpecificIds(AcceptedConnectionEntity acceptedConnectionEntity, UUID acceptedByUserId, UUID acceptedUserId) {
        try {
            return acceptedConnectionEntityFactory.create(
                    acceptedConnectionEntity.getId(),
                    acceptedByUserId,
                    acceptedUserId,
                    acceptedConnectionEntity.getCreatedAt()
            );
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    public AcceptedConnectionEntity withCreatedAt(AcceptedConnectionEntity acceptedConnectionEntity, Instant createdAt) {
        try {
            return acceptedConnectionEntityFactory.create(
                    acceptedConnectionEntity.getId(),
                    acceptedConnectionEntity.getAcceptedByUserId(),
                    acceptedConnectionEntity.getAcceptedUserId(),
                    createdAt
            );
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }
}
