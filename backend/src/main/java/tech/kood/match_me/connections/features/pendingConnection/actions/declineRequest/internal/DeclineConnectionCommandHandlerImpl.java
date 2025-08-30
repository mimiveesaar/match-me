package tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api.ConnectionRequestUndoEvent;
import tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api.DeclineConnectionCommandHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api.DeclineConnectionRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api.DeclineConnectionResults;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionCommandHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionResults;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionReasonDTO;


@ApplicationLayer
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
            return new DeclineConnectionResults.InvalidRequest(request.requestId(), InvalidInputErrorDTO.from(validationResults), request.tracingId());
        }

        var pendingConnectionEntityQueryResult = pendingConnectionRepository.findById(request.connectionIdDTO().value());

        if (pendingConnectionEntityQueryResult.isEmpty()) {
            return new DeclineConnectionResults.NotFound(request.requestId(), request.tracingId());
        }

        var pendingConnectionEntity = pendingConnectionEntityQueryResult.get();

        //Undo the pending connection request.
        if (pendingConnectionEntity.getSenderId().equals(request.declinedByUser().value())) {
            var pendingConnectionDeleted = pendingConnectionRepository.deleteById(pendingConnectionEntity.getId());

            if (!pendingConnectionDeleted) {
                return new DeclineConnectionResults.AlreadyDeclined(request.requestId(), request.tracingId());
            }

            eventPublisher.publishEvent(new ConnectionRequestUndoEvent(request.connectionIdDTO(), new UserIdDTO(pendingConnectionEntity.getSenderId()), new UserIdDTO(pendingConnectionEntity.getTargetId())));
            return new DeclineConnectionResults.Success(request.requestId(), request.tracingId());
        } else {
            var createRejectedConnectionRequest = new CreateRejectedConnectionRequest(request.declinedByUser(), new UserIdDTO(pendingConnectionEntity.getSenderId()), RejectedConnectionReasonDTO.CONNECTION_DECLINED, request.tracingId());

            var result = createRejectedConnectionCommandHandler.handle(createRejectedConnectionRequest);

            //Handle errors, in real application we would handle retries.
            if (!(result instanceof CreateRejectedConnectionResults.Success success)) {
                return new DeclineConnectionResults.SystemError(request.requestId(), "Something went wrong", request.tracingId());
            }

            var pendingConnectionDeleted = pendingConnectionRepository.deleteById(pendingConnectionEntity.getId());

            if (!pendingConnectionDeleted) {
                return new DeclineConnectionResults.AlreadyDeclined(request.requestId(), request.tracingId());
            }

            eventPublisher.publishEvent(new ConnectionRequestUndoEvent(request.connectionIdDTO(), new UserIdDTO(pendingConnectionEntity.getSenderId()), new UserIdDTO(pendingConnectionEntity.getTargetId())));
            return new DeclineConnectionResults.Success(request.requestId(), request.tracingId());
        }
    }
}