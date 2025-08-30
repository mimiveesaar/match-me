package tech.kood.match_me.connections.features.acceptedConnection.internal.mapper;

import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionIdFactory;
import tech.kood.match_me.connections.features.acceptedConnection.domain.api.AcceptedConnectionDTO;
import tech.kood.match_me.connections.features.acceptedConnection.domain.internal.AcceptedConnection;
import tech.kood.match_me.connections.features.acceptedConnection.domain.internal.AcceptedConnectionFactory;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity.AcceptedConnectionEntity;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity.AcceptedConnectionEntityFactory;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;

@Component
@ApplicationLayer
public class AcceptedConnectionMapper {

    private final AcceptedConnectionFactory acceptedConnectionFactory;
    private final AcceptedConnectionEntityFactory acceptedConnectionEntityFactory;

    private final ConnectionIdFactory connectionIdFactory;
    private final UserIdFactory userIdFactory;

    public AcceptedConnectionMapper(AcceptedConnectionFactory acceptedConnectionFactory,
            AcceptedConnectionEntityFactory acceptedConnectionEntityFactory,
            ConnectionIdFactory connectionIdFactory, UserIdFactory userIdFactory) {
        this.acceptedConnectionFactory = acceptedConnectionFactory;
        this.acceptedConnectionEntityFactory = acceptedConnectionEntityFactory;
        this.connectionIdFactory = connectionIdFactory;
        this.userIdFactory = userIdFactory;
    }

    public AcceptedConnection toAcceptedConnection(
            AcceptedConnectionEntity acceptedConnectionEntity)
            throws CheckedConstraintViolationException {

        var connectionId = connectionIdFactory.create(acceptedConnectionEntity.getId());
        var acceptedByUserId = userIdFactory.create(acceptedConnectionEntity.getAcceptedByUserId());
        var acceptedUserId = userIdFactory.create(acceptedConnectionEntity.getAcceptedUserId());

        return acceptedConnectionFactory.create(connectionId, acceptedByUserId, acceptedUserId,
                acceptedConnectionEntity.getCreatedAt());
    }

    public AcceptedConnection toAcceptedConnection(AcceptedConnectionDTO acceptedConnectionDTO)
            throws CheckedConstraintViolationException {

        var connectionId = connectionIdFactory.create(acceptedConnectionDTO.connectionIdDTO().value());
        var acceptedByUserId = userIdFactory.create(acceptedConnectionDTO.acceptedByUser().value());
        var acceptedUserId = userIdFactory.create(acceptedConnectionDTO.acceptedUser().value());
        var createdAt = acceptedConnectionDTO.createdAt();

        return acceptedConnectionFactory.create(connectionId, acceptedByUserId, acceptedUserId,
                createdAt);
    }

    public AcceptedConnectionEntity toEntity(AcceptedConnection acceptedConnection)
            throws CheckedConstraintViolationException {
        var connectionId = acceptedConnection.getId().getValue();
        var acceptedByUserId = acceptedConnection.getAcceptedByUser().getValue();
        var acceptedUserId = acceptedConnection.getAcceptedUser().getValue();

        return acceptedConnectionEntityFactory.create(connectionId, acceptedByUserId,
                acceptedUserId, acceptedConnection.getCreatedAt());
    }

    public AcceptedConnectionDTO toDTO(AcceptedConnection acceptedConnection) {
        var connectionIdDTO = new ConnectionIdDTO(acceptedConnection.getId().getValue());
        var acceptedByUserDTO = new UserIdDTO(acceptedConnection.getAcceptedByUser().getValue());
        var acceptedUserDTO = new UserIdDTO(acceptedConnection.getAcceptedUser().getValue());

        return new AcceptedConnectionDTO(connectionIdDTO, acceptedByUserDTO, acceptedUserDTO,
                acceptedConnection.getCreatedAt());
    }

    public AcceptedConnectionDTO toDTO(AcceptedConnectionEntity acceptedConnectionEntity)
            throws CheckedConstraintViolationException {
        var acceptedConnection = toAcceptedConnection(acceptedConnectionEntity);
        return toDTO(acceptedConnection);
    }
}
