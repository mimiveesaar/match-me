package tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api.ConnectionRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api.ConnectionRequestCommandHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api.ConnectionRequestResults;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.pendingConnection.domain.internal.PendingConnectionFactory;
import tech.kood.match_me.connections.features.pendingConnection.internal.mapper.PendingConnectionMapper;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;

@ApplicationLayer
@Service
public class ConnectionRequestCommandHandlerImpl implements ConnectionRequestCommandHandler {

    private final Validator validator;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final PendingConnectionFactory pendingConnectionFactory;
    private final PendingConnectionMapper pendingConnectionMapper;
    private final UserIdFactory userIdFactory;

    public ConnectionRequestCommandHandlerImpl(Validator validator, PendingConnectionRepository pendingConnectionRepository, PendingConnectionFactory pendingConnectionFactory, PendingConnectionMapper pendingConnectionMapper, UserIdFactory userIdFactory) {
        this.validator = validator;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.pendingConnectionFactory = pendingConnectionFactory;
        this.pendingConnectionMapper = pendingConnectionMapper;
        this.userIdFactory = userIdFactory;
    }

    @Override
    @Transactional
    public ConnectionRequestResults handle(ConnectionRequest request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new ConnectionRequestResults.InvalidRequest(request.requestId(), InvalidInputErrorDTO.from(validationResults), request.tracingId());
        }

        try {
            var findBetweenUsers = pendingConnectionRepository.findBetweenUsers(request.senderId().value(), request.targetId().value());

            if (findBetweenUsers.isPresent()) {
                return new ConnectionRequestResults.AlreadyExists(request.requestId(), request.tracingId());
            }

            var senderUserId = userIdFactory.from(request.senderId());
            var targetUserId = userIdFactory.from(request.targetId());
            var pendingConnection = pendingConnectionFactory.createNew(senderUserId, targetUserId);
            var pendingConnectionEntity = pendingConnectionMapper.toEntity(pendingConnection);

            pendingConnectionRepository.save(pendingConnectionEntity);

            return new ConnectionRequestResults.Success(request.requestId(), new ConnectionIdDTO(pendingConnection.getId().getValue()), request.tracingId());
        } catch (Exception ex) {
            return new ConnectionRequestResults.SystemError(request.requestId(), ex.getMessage(), request.tracingId());
        }
    }
}
