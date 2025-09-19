package tech.kood.match_me.connections.features.pendingConnection;

import org.springframework.stereotype.Component;
import tech.kood.match_me.common.domain.internal.userId.UserId;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.pendingConnection.domain.internal.PendingConnection;
import tech.kood.match_me.connections.features.pendingConnection.domain.internal.PendingConnectionFactory;

import java.time.Instant;

@Component
public class PendingConnectionMother {

    private final PendingConnectionFactory pendingConnectionFactory;
    private final UserIdFactory userIdFactory;

    public PendingConnectionMother(PendingConnectionFactory pendingConnectionFactory, UserIdFactory userIdFactory) {
        this.pendingConnectionFactory = pendingConnectionFactory;
        this.userIdFactory = userIdFactory;
    }

    public PendingConnection createPendingConnection() {

        //These tests are module specific, so we can create the ids directly.
        var senderId = userIdFactory.createNew();
        var targetId = userIdFactory.createNew();
        try {
            return pendingConnectionFactory.createNew(senderId, targetId);
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    public PendingConnection withSpecificIds(PendingConnection pendingConnection, UserId senderId, UserId targetId) {
        try {
            return pendingConnectionFactory.create(pendingConnection.getId(), senderId, targetId, pendingConnection.getCreatedAt());
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    public PendingConnection withCreatedAt(PendingConnection pendingConnection, Instant createdAt) {
        try {
            return pendingConnectionFactory.create(pendingConnection.getId(), pendingConnection.getSenderId(), pendingConnection.getTargetId(), createdAt);
        } catch (CheckedConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }
}
