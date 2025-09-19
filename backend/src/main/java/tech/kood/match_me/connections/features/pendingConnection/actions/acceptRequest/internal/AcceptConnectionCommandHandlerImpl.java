package tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InputFieldErrorDTO;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.CreateAcceptedConnectionCommandHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.CreateAcceptedConnectionRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.CreateAcceptedConnectionResults;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.AcceptConnectionCommandHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.AcceptConnectionRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.AcceptConnectionResults;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.ConnectionRequestAcceptedEvent;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;

import java.util.List;

@ApplicationLayer
@Service
public class AcceptConnectionCommandHandlerImpl implements AcceptConnectionCommandHandler {

    private final Validator validator;
    private final CreateAcceptedConnectionCommandHandler createAcceptedConnectionCommandHandler;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AcceptConnectionCommandHandlerImpl(Validator validator,
                                              CreateAcceptedConnectionCommandHandler createAcceptedConnectionCommandHandler,
                                              PendingConnectionRepository pendingConnectionRepository,
                                              ApplicationEventPublisher eventPublisher) {
        this.validator = validator;
        this.createAcceptedConnectionCommandHandler = createAcceptedConnectionCommandHandler;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public AcceptConnectionResults handle(AcceptConnectionRequest request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new AcceptConnectionResults.InvalidRequest(
                    InvalidInputErrorDTO.fromValidation(validationResults));
        }

        var pendingConnectionEntityQueryResult =
                pendingConnectionRepository.findById(request.connectionIdDTO().value());

        if (pendingConnectionEntityQueryResult.isEmpty()) {
            return new AcceptConnectionResults.NotFound();
        }

        var pendingConnectionEntity = pendingConnectionEntityQueryResult.get();

        if (!pendingConnectionEntity.getTargetId().equals(request.acceptedByUser().value())) {
            var fieldError =
                    new InputFieldErrorDTO("acceptedByUserId", request.acceptedByUser().value(),
                            "Only the target user can accept a connection request");
            return new AcceptConnectionResults.InvalidRequest(
                    new InvalidInputErrorDTO(List.of(fieldError)));
        }


        var connectionId = new ConnectionIdDTO(pendingConnectionEntity.getId());
        var createAcceptedConnectionRequest = new CreateAcceptedConnectionRequest(connectionId, request.acceptedByUser(), new UserIdDTO(pendingConnectionEntity.getSenderId()));

        var result = createAcceptedConnectionCommandHandler.handle(createAcceptedConnectionRequest);

        if (result instanceof CreateAcceptedConnectionResults.AlreadyExists) {
            return new AcceptConnectionResults.AlreadyAccepted();
        } else if (!(result instanceof CreateAcceptedConnectionResults.Success)) {
            return new AcceptConnectionResults.SystemError("Something went wrong");
        }

        var pendingConnectionDeleted =
                pendingConnectionRepository.deleteById(pendingConnectionEntity.getId());

        if (!pendingConnectionDeleted) {
            return new AcceptConnectionResults.AlreadyAccepted();
        }

        eventPublisher.publishEvent(new ConnectionRequestAcceptedEvent(request.connectionIdDTO(),
                new UserIdDTO(pendingConnectionEntity.getSenderId()),
                new UserIdDTO(pendingConnectionEntity.getTargetId())));

        return new AcceptConnectionResults.Success();
    }
}
