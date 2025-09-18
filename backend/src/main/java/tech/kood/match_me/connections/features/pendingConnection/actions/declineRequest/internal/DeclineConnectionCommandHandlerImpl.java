package tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api.*;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionCommandHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionResults;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionReasonDTO;


@ApplicationLayer
@Service
public class DeclineConnectionCommandHandlerImpl implements DeclineConnectionCommandHandler {

    private final Validator validator;
    private final CreateRejectedConnectionCommandHandler createRejectedConnectionCommandHandler;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public DeclineConnectionCommandHandlerImpl(Validator validator, CreateRejectedConnectionCommandHandler createRejectedConnectionCommandHandler, PendingConnectionRepository pendingConnectionRepository, ApplicationEventPublisher eventPublisher) {
        this.validator = validator;
        this.createRejectedConnectionCommandHandler = createRejectedConnectionCommandHandler;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public DeclineConnectionResults handle(DeclineConnectionRequest request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new DeclineConnectionResults.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        var pendingConnectionEntityQueryResult = pendingConnectionRepository.findById(request.connectionIdDTO().value());

        if (pendingConnectionEntityQueryResult.isEmpty()) {
            return new DeclineConnectionResults.NotFound();
        }

        var pendingConnectionEntity = pendingConnectionEntityQueryResult.get();

        //Undo the pending connection request.
        if (pendingConnectionEntity.getSenderId().equals(request.declinedByUser().value())) {
            var pendingConnectionDeleted = pendingConnectionRepository.deleteById(pendingConnectionEntity.getId());

            if (!pendingConnectionDeleted) {
                return new DeclineConnectionResults.AlreadyDeclined();
            }

            eventPublisher.publishEvent(new ConnectionRequestUndoEvent(request.connectionIdDTO(), new UserIdDTO(pendingConnectionEntity.getSenderId()), new UserIdDTO(pendingConnectionEntity.getTargetId())));
            return new DeclineConnectionResults.Success();
        }
        //Decline the pending connection request and create a rejected connection.
        else {
            var createRejectedConnectionRequest = new CreateRejectedConnectionRequest(request.declinedByUser(), new UserIdDTO(pendingConnectionEntity.getSenderId()), RejectedConnectionReasonDTO.CONNECTION_DECLINED);

            var result = createRejectedConnectionCommandHandler.handle(createRejectedConnectionRequest);

            //Handle errors, in real application we would handle retries.
            if (!(result instanceof CreateRejectedConnectionResults.Success success)) {
                return new DeclineConnectionResults.SystemError("Something went wrong");
            }

            var pendingConnectionDeleted = pendingConnectionRepository.deleteById(pendingConnectionEntity.getId());

            if (!pendingConnectionDeleted) {
                return new DeclineConnectionResults.AlreadyDeclined();
            }

            eventPublisher.publishEvent(new ConnectionRequestDeclinedEvent(request.connectionIdDTO(), new UserIdDTO(pendingConnectionEntity.getSenderId()), new UserIdDTO(pendingConnectionEntity.getTargetId())));
            return new DeclineConnectionResults.Success();
        }
    }
}