package tech.kood.match_me.connections.features.acceptedConnection.actions.internal;

import jakarta.validation.Validator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionIdFactory;
import tech.kood.match_me.connections.features.acceptedConnection.actions.CreateConnection;
import tech.kood.match_me.connections.features.acceptedConnection.domain.internal.AcceptedConnectionFactory;
import tech.kood.match_me.connections.features.acceptedConnection.internal.mapper.AcceptedConnectionMapper;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;

@Service
public class CreateConnectionHandlerImpl implements CreateConnection.Handler {

    private final Validator validator;

    private final AcceptedConnectionRepository acceptedConnectionRepository;

    private final AcceptedConnectionFactory acceptedConnectionFactory;

    private final UserIdFactory userIdFactory;

    private final ConnectionIdFactory connectionIdFactory;

    private final AcceptedConnectionMapper acceptedConnectionMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    public CreateConnectionHandlerImpl(Validator validator, AcceptedConnectionRepository acceptedConnectionRepository, AcceptedConnectionFactory acceptedConnectionFactory, UserIdFactory userIdFactory, ConnectionIdFactory connectionIdFactory, AcceptedConnectionMapper acceptedConnectionMapper, ApplicationEventPublisher applicationEventPublisher) {
        this.validator = validator;
        this.acceptedConnectionRepository = acceptedConnectionRepository;
        this.acceptedConnectionFactory = acceptedConnectionFactory;
        this.userIdFactory = userIdFactory;
        this.connectionIdFactory = connectionIdFactory;
        this.acceptedConnectionMapper = acceptedConnectionMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public CreateConnection.Result handle(CreateConnection.Request request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new CreateConnection.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        var existingConnection = acceptedConnectionRepository.findBetweenUsers(request.acceptedByUser().value(), request.acceptedUser().value());
        if (existingConnection.isPresent()) {
            return new CreateConnection.Result.AlreadyExists();
        }

        try {
            var acceptedByUser = userIdFactory.create(request.acceptedByUser().value());
            var acceptedUser = userIdFactory.create(request.acceptedUser().value());
            var connectionId = connectionIdFactory.create(request.connectionId().value());
            var acceptedConnection = acceptedConnectionFactory.createNew(connectionId, acceptedByUser, acceptedUser);
            var acceptedConnectionEntity = acceptedConnectionMapper.toEntity(acceptedConnection);

            acceptedConnectionRepository.save(acceptedConnectionEntity);
            applicationEventPublisher.publishEvent(new CreateConnection.AcceptedConnectionCreated(acceptedConnectionMapper.toDTO(acceptedConnection)));
            return new CreateConnection.Result.Success(new ConnectionIdDTO(acceptedConnection.getId().getValue()));
        }
        catch (CheckedConstraintViolationException e) {
            return new CreateConnection.Result.InvalidRequest(InvalidInputErrorDTO.fromException(e));
        }
        catch (Exception e) {
            return new CreateConnection.Result.SystemError("An unexpected error occurred while processing the request.");
        }
    }
}