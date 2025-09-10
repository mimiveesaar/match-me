package tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionIdFactory;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.CreateAcceptedConnectionCommandHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.CreateAcceptedConnectionRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.CreateAcceptedConnectionResults;
import tech.kood.match_me.connections.features.acceptedConnection.domain.internal.AcceptedConnectionFactory;
import tech.kood.match_me.connections.features.acceptedConnection.internal.mapper.AcceptedConnectionMapper;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;

@Service
@ApplicationLayer
public class CreateAcceptedConnectionCommandHandlerImpl
        implements CreateAcceptedConnectionCommandHandler {

    private final Validator validator;

    private final AcceptedConnectionRepository acceptedConnectionRepository;

    private final AcceptedConnectionFactory acceptedConnectionFactory;

    private final UserIdFactory userIdFactory;

    private final ConnectionIdFactory connectionIdFactory;

    private final AcceptedConnectionMapper acceptedConnectionMapper;

    public CreateAcceptedConnectionCommandHandlerImpl(Validator validator, AcceptedConnectionRepository acceptedConnectionRepository, AcceptedConnectionFactory acceptedConnectionFactory, UserIdFactory userIdFactory, ConnectionIdFactory connectionIdFactory, AcceptedConnectionMapper acceptedConnectionMapper) {
        this.validator = validator;
        this.acceptedConnectionRepository = acceptedConnectionRepository;
        this.acceptedConnectionFactory = acceptedConnectionFactory;
        this.userIdFactory = userIdFactory;
        this.connectionIdFactory = connectionIdFactory;
        this.acceptedConnectionMapper = acceptedConnectionMapper;
    }

    @Override
    public CreateAcceptedConnectionResults handle(CreateAcceptedConnectionRequest request) {
        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            return new CreateAcceptedConnectionResults.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        var existingConnection = acceptedConnectionRepository.findBetweenUsers(request.acceptedByUser().value(), request.acceptedUser().value());
        if (existingConnection.isPresent()) {
            return new CreateAcceptedConnectionResults.AlreadyExists();
        }

        try {
            var acceptedByUser = userIdFactory.create(request.acceptedByUser().value());
            var acceptedUser = userIdFactory.create(request.acceptedUser().value());
            var connectionId = connectionIdFactory.create(request.connectionId().value());
            var acceptedConnection = acceptedConnectionFactory.createNew(connectionId, acceptedByUser, acceptedUser);
            var acceptedConnectionEntity = acceptedConnectionMapper.toEntity(acceptedConnection);

            acceptedConnectionRepository.save(acceptedConnectionEntity);
            return new CreateAcceptedConnectionResults.Success(new ConnectionIdDTO(acceptedConnection.getId().getValue()));
        }
        catch (CheckedConstraintViolationException e) {
            return new CreateAcceptedConnectionResults.InvalidRequest(InvalidInputErrorDTO.fromException(e));
        }
        catch (Exception e) {
            return new CreateAcceptedConnectionResults.SystemError("An unexpected error occurred while processing the request.");
        }

    }
}
