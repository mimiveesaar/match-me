package tech.kood.match_me.connections.features.rejectedConnection.internal.mapper;

import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionIdFactory;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionDTO;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionReasonDTO;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionFactory;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionReason;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity.RejectedConnectionEntity;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity.RejectedConnectionEntityFactory;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;

@Component
@ApplicationLayer
public class RejectedConnectionMapper {

    private final RejectedConnectionFactory rejectedConnectionFactory;
    private final RejectedConnectionEntityFactory rejectedConnectionEntityFactory;

    private final ConnectionIdFactory connectionIdFactory;
    private final UserIdFactory userIdFactory;

    public RejectedConnectionMapper(RejectedConnectionFactory rejectedConnectionFactory,
            RejectedConnectionEntityFactory rejectedConnectionEntityFactory,
            ConnectionIdFactory connectionIdFactory, UserIdFactory userIdFactory) {
        this.rejectedConnectionFactory = rejectedConnectionFactory;
        this.rejectedConnectionEntityFactory = rejectedConnectionEntityFactory;
        this.connectionIdFactory = connectionIdFactory;
        this.userIdFactory = userIdFactory;
    }

    public RejectedConnection toRejectedConnection(
            RejectedConnectionEntity rejectedConnectionEntity)
            throws CheckedConstraintViolationException {

        var connectionId = connectionIdFactory.create(rejectedConnectionEntity.getId());
        var rejectedByUserId = userIdFactory.create(rejectedConnectionEntity.getRejectedByUserId());
        var rejectedUserId = userIdFactory.create(rejectedConnectionEntity.getRejectedUserId());

        return rejectedConnectionFactory.create(connectionId, rejectedByUserId, rejectedUserId,
                rejectedConnectionEntity.getReason(), rejectedConnectionEntity.getCreatedAt());
    }

    public RejectedConnection toRejectedConnection(RejectedConnectionDTO rejectedConnectionDTO)
            throws CheckedConstraintViolationException {

        var connectionId =
                connectionIdFactory.create(rejectedConnectionDTO.connectionId().value());
        var rejectedByUserId = userIdFactory.create(rejectedConnectionDTO.rejectedByUser().value());
        var rejectedUserId = userIdFactory.create(rejectedConnectionDTO.rejectedUser().value());
        var reason = RejectedConnectionReason.valueOf(rejectedConnectionDTO.reason().name());
        var createdAt = rejectedConnectionDTO.createdAt();

        return rejectedConnectionFactory.create(connectionId, rejectedByUserId, rejectedUserId,
                reason, createdAt); // createdAt is not in DTO, might need to handle separately
    }

    public RejectedConnectionEntity toEntity(RejectedConnection rejectedConnection)
            throws CheckedConstraintViolationException {
        var connectionId = rejectedConnection.getId().getValue();
        var rejectedByUserId = rejectedConnection.getRejectedByUser().getValue();
        var rejectedUserId = rejectedConnection.getRejectedUser().getValue();

        return rejectedConnectionEntityFactory.create(connectionId, rejectedByUserId,
                rejectedUserId, rejectedConnection.getReason(), rejectedConnection.getCreatedAt());
    }

    public RejectedConnectionDTO toDTO(RejectedConnection rejectedConnection) {
        var connectionIdDTO = new ConnectionIdDTO(rejectedConnection.getId().getValue());
        var rejectedByUserDTO = new UserIdDTO(rejectedConnection.getRejectedByUser().getValue());
        var rejectedUserDTO = new UserIdDTO(rejectedConnection.getRejectedUser().getValue());
        var reasonDTO = RejectedConnectionReasonDTO.valueOf(rejectedConnection.getReason().name());

        return new RejectedConnectionDTO(connectionIdDTO, rejectedByUserDTO, rejectedUserDTO,
                reasonDTO, rejectedConnection.getCreatedAt());
    }

    public RejectedConnectionDTO toDTO(RejectedConnectionEntity rejectedConnectionEntity)
            throws CheckedConstraintViolationException {
        var rejectedConnection = toRejectedConnection(rejectedConnectionEntity);
        return toDTO(rejectedConnection);
    }
}
