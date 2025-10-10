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
import tech.kood.match_me.connections.features.pendingConnection.actions.AcceptConnection;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;

import java.util.List;

@ApplicationLayer
@Service
public class AcceptConnectionCommandHandlerImpl implements AcceptConnection.Handler {

    private final Validator validator;
    private final AcceptConnection.Handler createAcceptedConnectionCommandHandler;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AcceptConnectionCommandHandlerImpl(Validator validator,
                                              AcceptConnection.Handler createAcceptedConnectionCommandHandler,
                                              PendingConnectionRepository pendingConnectionRepository,
                                              ApplicationEventPublisher eventPublisher) {
        this.validator = validator;
        this.createAcceptedConnectionCommandHandler = createAcceptedConnectionCommandHandler;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.eventPublisher = eventPublisher;
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
        var createAcceptedConnectionRequest = new AcceptConnection.Request(connectionId, request.acceptedByUser(), new UserIdDTO(pendingConnectionEntity.getSenderId()));

        var result = createAcceptedConnectionCommandHandler.handle(createAcceptedConnectionRequest);

        if (result instanceof AcceptConnection.Result.AlreadyAccepted) {
            return new AcceptConnection.Result.AlreadyAccepted();
        } else if (!(result instanceof AcceptConnection.Result.Success)) {
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
