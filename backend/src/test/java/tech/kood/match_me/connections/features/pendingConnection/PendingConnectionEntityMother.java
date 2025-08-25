package tech.kood.match_me.connections.features.pendingConnection;

import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntityFactory;

import java.time.Instant;
import java.util.UUID;

@Component
public class PendingConnectionEntityMother {

    private final PendingConnectionEntityFactory pendingConnectionEntityFactory;

    public PendingConnectionEntityMother(PendingConnectionEntityFactory pendingConnectionEntityFactory) {
        this.pendingConnectionEntityFactory = pendingConnectionEntityFactory;
    }

    public PendingConnectionEntity createPendingConnectionEntity() {
        try {
            return pendingConnectionEntityFactory.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Instant.now());
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    public PendingConnectionEntity withSpecificIds(PendingConnectionEntity pendingConnectionEntity, UUID senderId, UUID targetId) {
        try {
            return pendingConnectionEntityFactory.create(pendingConnectionEntity.getId(), senderId, targetId, pendingConnectionEntity.getCreatedAt());
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    public PendingConnectionEntity withCreatedAt(PendingConnectionEntity pendingConnectionEntity, Instant createdAt) {
        try {
            return pendingConnectionEntityFactory.create(pendingConnectionEntity.getId(), pendingConnectionEntity.getSenderId(), pendingConnectionEntity.getTargetId(), createdAt);
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }
}
