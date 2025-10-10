package tech.kood.match_me.connections.features.pendingConnection.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.CreateRequest;
import tech.kood.match_me.connections.features.pendingConnection.domain.internal.PendingConnectionFactory;
import tech.kood.match_me.connections.features.pendingConnection.internal.mapper.PendingConnectionMapper;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;

@ApplicationLayer
@Service
public class ConnectionRequestCommandHandlerImpl implements CreateRequest.Handler {

    private final Validator validator;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final PendingConnectionFactory pendingConnectionFactory;
    private final PendingConnectionMapper pendingConnectionMapper;
    private final UserIdFactory userIdFactory;
    private final ApplicationEventPublisher eventPublisher;

    public ConnectionRequestCommandHandlerImpl(Validator validator, PendingConnectionRepository pendingConnectionRepository, PendingConnectionFactory pendingConnectionFactory, PendingConnectionMapper pendingConnectionMapper, UserIdFactory userIdFactory, ApplicationEventPublisher eventPublisher) {
        this.validator = validator;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.pendingConnectionFactory = pendingConnectionFactory;
        this.pendingConnectionMapper = pendingConnectionMapper;
        this.userIdFactory = userIdFactory;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public CreateRequest.Result handle(CreateRequest.Request request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new CreateRequest.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            var findBetweenUsers = pendingConnectionRepository.findBetweenUsers(request.senderId().value(), request.targetId().value());

            if (findBetweenUsers.isPresent()) {
                return new CreateRequest.Result.AlreadyExists();
            }

            var senderUserId = userIdFactory.from(request.senderId());
            var targetUserId = userIdFactory.from(request.targetId());
            var pendingConnection = pendingConnectionFactory.createNew(senderUserId, targetUserId);
            var pendingConnectionEntity = pendingConnectionMapper.toEntity(pendingConnection);

            pendingConnectionRepository.save(pendingConnectionEntity);

            var connectionId = new ConnectionIdDTO(pendingConnection.getId().getValue());
            eventPublisher.publishEvent(new CreateRequest.ConnectionRequestCreated(connectionId, request.senderId(), request.targetId()));

            return new CreateRequest.Result.Success(connectionId);
        } catch (Exception ex) {
            return new CreateRequest.Result.SystemError(ex.getMessage());
        }
    }
}