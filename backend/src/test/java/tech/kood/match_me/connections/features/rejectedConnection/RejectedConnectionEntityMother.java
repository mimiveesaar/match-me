package tech.kood.match_me.connections.features.rejectedConnection;

import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionReason;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity.RejectedConnectionEntity;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity.RejectedConnectionEntityFactory;

import java.time.Instant;
import java.util.UUID;

@Component
public class RejectedConnectionEntityMother {

    private final RejectedConnectionEntityFactory rejectedConnectionEntityFactory;

    public RejectedConnectionEntityMother(RejectedConnectionEntityFactory rejectedConnectionEntityFactory) {
        this.rejectedConnectionEntityFactory = rejectedConnectionEntityFactory;
    }

    public RejectedConnectionEntity createRejectedConnectionEntity() {
        try {
            return rejectedConnectionEntityFactory.create(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    RejectedConnectionReason.CONNECTION_DECLINED,
                    Instant.now()
            );
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    public RejectedConnectionEntity withSpecificIds(RejectedConnectionEntity rejectedConnectionEntity, UUID rejectedByUserId, UUID rejectedUserId) {
        try {
            return rejectedConnectionEntityFactory.create(
                    rejectedConnectionEntity.getId(),
                    rejectedByUserId,
                    rejectedUserId,
                    rejectedConnectionEntity.getReason(),
                    rejectedConnectionEntity.getCreatedAt()
            );
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    public RejectedConnectionEntity withReason(RejectedConnectionEntity rejectedConnectionEntity, RejectedConnectionReason reason) {
        try {
            return rejectedConnectionEntityFactory.create(
                    rejectedConnectionEntity.getId(),
                    rejectedConnectionEntity.getRejectedByUserId(),
                    rejectedConnectionEntity.getRejectedUserId(),
                    reason,
                    rejectedConnectionEntity.getCreatedAt()
            );
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    public RejectedConnectionEntity withCreatedAt(RejectedConnectionEntity rejectedConnectionEntity, Instant createdAt) {
        try {
            return rejectedConnectionEntityFactory.create(
                    rejectedConnectionEntity.getId(),
                    rejectedConnectionEntity.getRejectedByUserId(),
                    rejectedConnectionEntity.getRejectedUserId(),
                    rejectedConnectionEntity.getReason(),
                    createdAt
            );
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }
}
