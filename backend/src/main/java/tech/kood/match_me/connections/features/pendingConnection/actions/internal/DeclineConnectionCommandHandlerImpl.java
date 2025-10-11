package tech.kood.match_me.connections.features.pendingConnection.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.DeclineRequest;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.rejectedConnection.actions.CreateRejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionReasonDTO;


@ApplicationLayer
@Service
public class DeclineConnectionCommandHandlerImpl implements DeclineRequest.Handler {

    private final Validator validator;
    private final CreateRejectedConnection.Handler createRejectedConnectionHandler;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public DeclineConnectionCommandHandlerImpl(Validator validator, CreateRejectedConnection.Handler createRejectedConnectionHandler, PendingConnectionRepository pendingConnectionRepository, ApplicationEventPublisher eventPublisher) {
        this.validator = validator;
        this.createRejectedConnectionHandler = createRejectedConnectionHandler;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public DeclineRequest.Result handle(DeclineRequest.Request request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new DeclineRequest.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        var pendingConnectionEntityQueryResult = pendingConnectionRepository.findById(request.connectionIdDTO().value());

        if (pendingConnectionEntityQueryResult.isEmpty()) {
            return new DeclineRequest.Result.NotFound();
        }

        var pendingConnectionEntity = pendingConnectionEntityQueryResult.get();

        //Undo the pending connection request.
        if (pendingConnectionEntity.getSenderId().equals(request.declinedByUser().value())) {
            var pendingConnectionDeleted = pendingConnectionRepository.deleteById(pendingConnectionEntity.getId());

            if (!pendingConnectionDeleted) {
                return new DeclineRequest.Result.AlreadyDeclined();
            }

            eventPublisher.publishEvent(new DeclineRequest.ConnectionRequestUndo(request.connectionIdDTO(), new UserIdDTO(pendingConnectionEntity.getSenderId()), new UserIdDTO(pendingConnectionEntity.getTargetId())));
            return new DeclineRequest.Result.Success();
        }
        //Decline the pending connection request and create a rejected connection.
        else {
            var createRejectedConnectionRequest = new CreateRejectedConnection.Request(request.declinedByUser(), new UserIdDTO(pendingConnectionEntity.getSenderId()), RejectedConnectionReasonDTO.CONNECTION_DECLINED);

            var result = createRejectedConnectionHandler.handle(createRejectedConnectionRequest);

            //Handle errors, in real application we would handle retries.
            if (!(result instanceof CreateRejectedConnection.Result.Success success)) {
                return new DeclineRequest.Result.SystemError("Something went wrong");
            }

            var pendingConnectionDeleted = pendingConnectionRepository.deleteById(pendingConnectionEntity.getId());

            if (!pendingConnectionDeleted) {
                return new DeclineRequest.Result.AlreadyDeclined();
            }

            eventPublisher.publishEvent(new DeclineRequest.ConnectionRequestDeclined(request.connectionIdDTO(), new UserIdDTO(pendingConnectionEntity.getSenderId()), new UserIdDTO(pendingConnectionEntity.getTargetId())));
            return new DeclineRequest.Result.Success();
        }
    }
}