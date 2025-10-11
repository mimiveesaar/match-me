package tech.kood.match_me.connections.features.pendingConnection.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InputFieldErrorDTO;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.acceptedConnection.actions.CreateAcceptedConnection;
import tech.kood.match_me.connections.features.pendingConnection.actions.AcceptConnectionRequest;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;

import java.util.List;

@ApplicationLayer
@Service
public class AcceptConnectionCommandHandlerImpl implements AcceptConnectionRequest.Handler {

    private final Validator validator;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CreateAcceptedConnection.Handler createAcceptedConnectionCommandHandler;

    public AcceptConnectionCommandHandlerImpl(Validator validator,
                                              PendingConnectionRepository pendingConnectionRepository,
                                              ApplicationEventPublisher eventPublisher, CreateAcceptedConnection.Handler createAcceptedConnectionCommandHandler1) {
        this.validator = validator;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.eventPublisher = eventPublisher;
        this.createAcceptedConnectionCommandHandler = createAcceptedConnectionCommandHandler1;
    }

    @Transactional
    @Override
    public AcceptConnectionRequest.Result handle(AcceptConnectionRequest.Request request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new AcceptConnectionRequest.Result.InvalidRequest(
                    InvalidInputErrorDTO.fromValidation(validationResults));
        }

        var pendingConnectionEntityQueryResult =
                pendingConnectionRepository.findById(request.connectionIdDTO().value());

        if (pendingConnectionEntityQueryResult.isEmpty()) {
            return new AcceptConnectionRequest.Result.NotFound();
        }

        var pendingConnectionEntity = pendingConnectionEntityQueryResult.get();

        if (!pendingConnectionEntity.getTargetId().equals(request.acceptedByUser().value())) {
            var fieldError =
                    new InputFieldErrorDTO("acceptedByUserId", request.acceptedByUser().value(),
                            "Only the target user can accept a connection request");
            return new AcceptConnectionRequest.Result.InvalidRequest(
                    new InvalidInputErrorDTO(List.of(fieldError)));
        }


        var connectionId = new ConnectionIdDTO(pendingConnectionEntity.getId());

        var createAcceptedConnectionRequest = new CreateAcceptedConnection.Request(connectionId, UserIdDTO.from(pendingConnectionEntity.getTargetId()), UserIdDTO.from(pendingConnectionEntity.getSenderId()));
        var result = createAcceptedConnectionCommandHandler.handle(createAcceptedConnectionRequest);

        if (result instanceof CreateAcceptedConnection.Result.AlreadyExists) {
            return new AcceptConnectionRequest.Result.AlreadyAccepted();
        } else if (!(result instanceof CreateAcceptedConnection.Result.Success)) {
            return new AcceptConnectionRequest.Result.SystemError("Something went wrong");
        }

        var pendingConnectionDeleted =
                pendingConnectionRepository.deleteById(pendingConnectionEntity.getId());

        if (!pendingConnectionDeleted) {
            return new AcceptConnectionRequest.Result.AlreadyAccepted();
        }

        eventPublisher.publishEvent(new AcceptConnectionRequest.ConnectionRequestAccepted(request.connectionIdDTO(),
                new UserIdDTO(pendingConnectionEntity.getSenderId()),
                new UserIdDTO(pendingConnectionEntity.getTargetId())));

        return new AcceptConnectionRequest.Result.Success();
    }
}