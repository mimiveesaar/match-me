package tech.kood.match_me.connections.features.acceptedConnection.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.acceptedConnection.actions.RejectConnection;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionReasonDTO;

import java.util.UUID;

@Component
@ApplicationLayer
public class RejectAcceptedConnectionCommandHandlerImpl
        implements RejectConnection.Handler {

    private final AcceptedConnectionRepository acceptedConnectionRepository;
    private final RejectConnection.Handler createRejectedConnectionCommandHandler;
    private final Validator validator;

    public RejectAcceptedConnectionCommandHandlerImpl(
            AcceptedConnectionRepository acceptedConnectionRepository, RejectConnection.Handler createRejectedConnectionCommandHandler, Validator validator) {
        this.acceptedConnectionRepository = acceptedConnectionRepository;
        this.createRejectedConnectionCommandHandler = createRejectedConnectionCommandHandler;
        this.validator = validator;
    }

    @Override
    @Transactional
    public RejectConnection.Result handle(RejectConnection.Request request) {

        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            return new RejectConnection.Result.InvalidRequest(
                    InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            UUID connectionId = request.connectionId().value();

            // Check if the accepted connection exists
            var existingAcceptedConnectionQueryResult = acceptedConnectionRepository.findById(connectionId);

            if (existingAcceptedConnectionQueryResult.isEmpty()) {
                return new RejectConnection.Result.NotFound();
            }

            var acceptedConnection = existingAcceptedConnectionQueryResult.get();
            var rejectedByUserId = request.rejectedBy();
            var rejectedUser = new UserIdDTO(acceptedConnection.getAcceptedByUserId().equals(rejectedByUserId.value()) ?
                    acceptedConnection.getAcceptedUserId() : acceptedConnection.getAcceptedByUserId());


            var createRejectedConnectionRequest = new RejectConnection.Request(rejectedByUserId, rejectedUser, RejectedConnectionReasonDTO.CONNECTION_REMOVED);
            var createRejectedConnectionResult = createRejectedConnectionCommandHandler.handle(createRejectedConnectionRequest);

            if (createRejectedConnectionResult instanceof RejectConnection.Result.SystemError systemError) {
                return new RejectConnection.Result.SystemError(systemError.message());
            }

            if (createRejectedConnectionResult instanceof RejectConnection.Result.InvalidRequest invalidRequest) {
                return new RejectConnection.Result.InvalidRequest(invalidRequest.error());
            }

            if (createRejectedConnectionResult instanceof RejectConnection.Result.AlreadyRejected) {
                return new RejectConnection.Result.AlreadyRejected();
            }

            // Delete the accepted connection (rejecting it)
            acceptedConnectionRepository.deleteById(connectionId);

            return new RejectConnection.Result.Success();

        } catch (Exception e) {
            return new RejectConnection.Result.SystemError(e.getMessage());
        }
    }
}
