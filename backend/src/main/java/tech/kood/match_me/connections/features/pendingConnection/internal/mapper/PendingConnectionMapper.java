package tech.kood.match_me.connections.features.pendingConnection.internal.mapper;

import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionIdFactory;
import tech.kood.match_me.connections.features.pendingConnection.domain.api.PendingConnectionDTO;
import tech.kood.match_me.connections.features.pendingConnection.domain.internal.PendingConnection;
import tech.kood.match_me.connections.features.pendingConnection.domain.internal.PendingConnectionFactory;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntityFactory;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;

@Component
@ApplicationLayer
public class PendingConnectionMapper {

    private final PendingConnectionFactory pendingConnectionFactory;
    private final PendingConnectionEntityFactory pendingConnectionEntityFactory;

    private final ConnectionIdFactory connectionIdFactory;
    private final UserIdFactory userIdFactory;


    public PendingConnectionMapper(PendingConnectionFactory pendingConnectionFactory, PendingConnectionEntityFactory pendingConnectionEntityFactory, ConnectionIdFactory connectionIdFactory, UserIdFactory userIdFactory) {
        this.pendingConnectionFactory = pendingConnectionFactory;
        this.pendingConnectionEntityFactory = pendingConnectionEntityFactory;
        this.connectionIdFactory = connectionIdFactory;
        this.userIdFactory = userIdFactory;
    }

    public PendingConnection toPendingConnection(PendingConnectionEntity pendingConnectionEntity) throws CheckedConstraintViolationException {

        var connectionId = connectionIdFactory.create(pendingConnectionEntity.getId());
        var senderId = userIdFactory.create(pendingConnectionEntity.getSenderId());
        var targetId = userIdFactory.create(pendingConnectionEntity.getTargetId());

        return pendingConnectionFactory.create(
                connectionId,
                senderId,
                targetId,
                pendingConnectionEntity.getCreatedAt());
    }

    public PendingConnection toPendingConnection(PendingConnectionDTO pendingConnectionDTO) throws CheckedConstraintViolationException {

        var connectionId = connectionIdFactory.create(pendingConnectionDTO.connectionIdDTO().value());
        var senderId = userIdFactory.create(pendingConnectionDTO.senderId().value());
        var targetId = userIdFactory.create(pendingConnectionDTO.targetId().value());

        return pendingConnectionFactory.create(
                connectionId,
                senderId,
                targetId,
                pendingConnectionDTO.createdAt());
    }

    public PendingConnectionEntity toEntity(PendingConnection pendingConnection) throws CheckedConstraintViolationException {
        var connectionId = pendingConnection.getId().getValue();
        var senderId = pendingConnection.getSenderId().getValue();
        var targetId = pendingConnection.getTargetId().getValue();

        return pendingConnectionEntityFactory.create(
                connectionId,
                senderId,
                targetId,
                pendingConnection.getCreatedAt());
    }

    public PendingConnectionDTO toDTO(PendingConnection pendingConnection) {
        var connectionIdDTO = new ConnectionIdDTO(pendingConnection.getId().getValue());
        var senderIdDTO = new UserIdDTO(pendingConnection.getSenderId().getValue());
        var targetIdDTO = new UserIdDTO(pendingConnection.getTargetId().getValue());

        return new PendingConnectionDTO(
                connectionIdDTO,
                senderIdDTO,
                targetIdDTO,
                pendingConnection.getCreatedAt()
        );
    }

    public PendingConnectionDTO toDTO(PendingConnectionEntity pendingConnectionEntity) throws CheckedConstraintViolationException {
        var pendingConnection = toPendingConnection(pendingConnectionEntity);
        return toDTO(pendingConnection);
    }
}