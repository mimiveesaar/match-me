package tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api.RejectAcceptedConnectionCommandHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api.RejectAcceptedConnectionRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api.RejectAcceptedConnectionResults;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionCommandHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionResults;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionReasonDTO;

import java.util.UUID;

@Component
@ApplicationLayer
public class RejectAcceptedConnectionCommandHandlerImpl
        implements RejectAcceptedConnectionCommandHandler {

    private final AcceptedConnectionRepository acceptedConnectionRepository;
    private final CreateRejectedConnectionCommandHandler createRejectedConnectionCommandHandler;
    private final Validator validator;

    public RejectAcceptedConnectionCommandHandlerImpl(
            AcceptedConnectionRepository acceptedConnectionRepository, CreateRejectedConnectionCommandHandler createRejectedConnectionCommandHandler, Validator validator) {
        this.acceptedConnectionRepository = acceptedConnectionRepository;
        this.createRejectedConnectionCommandHandler = createRejectedConnectionCommandHandler;
        this.validator = validator;
    }

    @Override
    @Transactional
    public RejectAcceptedConnectionResults handle(RejectAcceptedConnectionRequest request) {

        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            return new RejectAcceptedConnectionResults.InvalidRequest(
                    InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            UUID connectionId = request.connectionId().value();

            // Check if the accepted connection exists
            var existingAcceptedConnectionQueryResult = acceptedConnectionRepository.findById(connectionId);

            if (existingAcceptedConnectionQueryResult.isEmpty()) {
                return new RejectAcceptedConnectionResults.NotFound();
            }

            var acceptedConnection = existingAcceptedConnectionQueryResult.get();
            var rejectedByUserId = request.rejectedBy();
            var rejectedUser = new UserIdDTO(acceptedConnection.getAcceptedByUserId().equals(rejectedByUserId.value()) ?
                    acceptedConnection.getAcceptedUserId() : acceptedConnection.getAcceptedByUserId());


            var createRejectedConnectionRequest = new CreateRejectedConnectionRequest(rejectedByUserId, rejectedUser, RejectedConnectionReasonDTO.CONNECTION_REMOVED);
            var createRejectedConnectionResult = createRejectedConnectionCommandHandler.handle(createRejectedConnectionRequest);

            if (createRejectedConnectionResult instanceof CreateRejectedConnectionResults.SystemError systemError) {
                return new RejectAcceptedConnectionResults.SystemError(systemError.message());
            }

            if (createRejectedConnectionResult instanceof CreateRejectedConnectionResults.InvalidRequest invalidRequest) {
                return new RejectAcceptedConnectionResults.InvalidRequest(invalidRequest.error());
            }

            if (createRejectedConnectionResult instanceof CreateRejectedConnectionResults.AlreadyExists) {
                return new RejectAcceptedConnectionResults.AlreadyRejected();
            }

            // Delete the accepted connection (rejecting it)
            acceptedConnectionRepository.deleteById(connectionId);

            return new RejectAcceptedConnectionResults.Success();

        } catch (Exception e) {
            return new RejectAcceptedConnectionResults.SystemError(e.getMessage());
        }
    }
}
