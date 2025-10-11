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
import tech.kood.match_me.connections.features.acceptedConnection.actions.internal.CreateAcceptedConnectionHandlerImpl;
import tech.kood.match_me.connections.features.pendingConnection.actions.AcceptConnection;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;

import java.util.List;

@ApplicationLayer
@Service
public class AcceptConnectionCommandHandlerImpl implements AcceptConnection.Handler {

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
    public AcceptConnection.Result handle(AcceptConnection.Request request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new AcceptConnection.Result.InvalidRequest(
                    InvalidInputErrorDTO.fromValidation(validationResults));
        }

        var pendingConnectionEntityQueryResult =
                pendingConnectionRepository.findById(request.connectionIdDTO().value());

        if (pendingConnectionEntityQueryResult.isEmpty()) {
            return new AcceptConnection.Result.NotFound();
        }

        var pendingConnectionEntity = pendingConnectionEntityQueryResult.get();

        if (!pendingConnectionEntity.getTargetId().equals(request.acceptedByUser().value())) {
            var fieldError =
                    new InputFieldErrorDTO("acceptedByUserId", request.acceptedByUser().value(),
                            "Only the target user can accept a connection request");
            return new AcceptConnection.Result.InvalidRequest(
                    new InvalidInputErrorDTO(List.of(fieldError)));
        }


        var connectionId = new ConnectionIdDTO(pendingConnectionEntity.getId());

        var createAcceptedConnectionRequest = new CreateAcceptedConnection.Request(connectionId, UserIdDTO.from(pendingConnectionEntity.getTargetId()), UserIdDTO.from(pendingConnectionEntity.getSenderId()));
        var result = createAcceptedConnectionCommandHandler.handle(createAcceptedConnectionRequest);

        if (result instanceof CreateAcceptedConnection.Result.AlreadyExists) {
            return new AcceptConnection.Result.AlreadyAccepted();
        } else if (!(result instanceof CreateAcceptedConnection.Result.Success)) {
            return new AcceptConnection.Result.SystemError("Something went wrong");
        }

        var pendingConnectionDeleted =
                pendingConnectionRepository.deleteById(pendingConnectionEntity.getId());

        if (!pendingConnectionDeleted) {
            return new AcceptConnection.Result.AlreadyAccepted();
        }

        eventPublisher.publishEvent(new AcceptConnection.ConnectionRequestAccepted(request.connectionIdDTO(),
                new UserIdDTO(pendingConnectionEntity.getSenderId()),
                new UserIdDTO(pendingConnectionEntity.getTargetId())));

        return new AcceptConnection.Result.Success();
    }
}
