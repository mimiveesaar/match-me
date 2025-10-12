package tech.kood.match_me.connections.features.acceptedConnection.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.acceptedConnection.actions.RejectAcceptedConnection;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;
import tech.kood.match_me.connections.features.rejectedConnection.actions.CreateRejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionReasonDTO;

import java.util.UUID;

@ApplicationLayer
@Service
public class RejectAcceptedConnectionCommandHandlerImpl
        implements RejectAcceptedConnection.Handler {

    private final AcceptedConnectionRepository acceptedConnectionRepository;
    private final CreateRejectedConnection.Handler createRejectedConnectionCommandHandler;
    private final Validator validator;

    public RejectAcceptedConnectionCommandHandlerImpl(
            AcceptedConnectionRepository acceptedConnectionRepository, CreateRejectedConnection.Handler createRejectedConnectionCommandHandler, Validator validator) {
        this.acceptedConnectionRepository = acceptedConnectionRepository;
        this.createRejectedConnectionCommandHandler = createRejectedConnectionCommandHandler;
        this.validator = validator;
    }

    @Override
    @Transactional
    public RejectAcceptedConnection.Result handle(RejectAcceptedConnection.Request request) {

        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            return new RejectAcceptedConnection.Result.InvalidRequest(
                    InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            UUID connectionId = request.connectionId().value();

            // Check if the accepted connection exists
            var existingAcceptedConnectionQueryResult = acceptedConnectionRepository.findById(connectionId);

            if (existingAcceptedConnectionQueryResult.isEmpty()) {
                return new RejectAcceptedConnection.Result.NotFound();
            }

            var acceptedConnection = existingAcceptedConnectionQueryResult.get();
            var rejectedByUserId = request.rejectedBy();
            var rejectedUser = new UserIdDTO(acceptedConnection.getAcceptedByUserId().equals(rejectedByUserId.value()) ?
                    acceptedConnection.getAcceptedUserId() : acceptedConnection.getAcceptedByUserId());


            var createRejectedConnectionRequest = new CreateRejectedConnection.Request(rejectedByUserId, rejectedUser, RejectedConnectionReasonDTO.CONNECTION_REMOVED);
            var createRejectedConnectionResult = createRejectedConnectionCommandHandler.handle(createRejectedConnectionRequest);

            if (createRejectedConnectionResult instanceof CreateRejectedConnection.Result.SystemError systemError) {
                return new RejectAcceptedConnection.Result.SystemError(systemError.message());
            }

            if (createRejectedConnectionResult instanceof CreateRejectedConnection.Result.InvalidRequest invalidRequest) {
                return new RejectAcceptedConnection.Result.InvalidRequest(invalidRequest.error());
            }

            if (createRejectedConnectionResult instanceof CreateRejectedConnection.Result.AlreadyExists alreadyExists) {
                return new RejectAcceptedConnection.Result.AlreadyRejected();
            }

            // Delete the accepted connection (rejecting it)
            acceptedConnectionRepository.deleteById(connectionId);

            return new RejectAcceptedConnection.Result.Success();

        } catch (Exception e) {
            return new RejectAcceptedConnection.Result.SystemError(e.getMessage());
        }
    }
}
