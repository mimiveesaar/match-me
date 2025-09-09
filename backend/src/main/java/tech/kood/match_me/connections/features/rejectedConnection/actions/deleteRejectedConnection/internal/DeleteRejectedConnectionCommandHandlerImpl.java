package tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api.DeleteRejectedConnectionRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api.DeleteRejectedConnectionCommandHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api.DeleteRejectedConnectionResults;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;

import java.util.UUID;

@Component
@ApplicationLayer
public class DeleteRejectedConnectionCommandHandlerImpl
        implements DeleteRejectedConnectionCommandHandler {

    private final RejectedConnectionRepository rejectedConnectionRepository;
    private final Validator validator;

    public DeleteRejectedConnectionCommandHandlerImpl(
            RejectedConnectionRepository rejectedConnectionRepository, Validator validator) {
        this.rejectedConnectionRepository = rejectedConnectionRepository;
        this.validator = validator;
    }

    @Override
    @Transactional
    public DeleteRejectedConnectionResults handle(DeleteRejectedConnectionRequest request) {

        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            return new DeleteRejectedConnectionResults.InvalidRequest(request.requestId(), InvalidInputErrorDTO.fromValidation(validationResults), request.tracingId());
        }

        try {
            UUID connectionId = request.connectionIdDTO().value();

            // Check if the rejected connection exists
            var existingRejection = rejectedConnectionRepository.findById(connectionId);

            if (existingRejection.isEmpty()) {
                return new DeleteRejectedConnectionResults.NotFound(request.requestId(),
                        request.tracingId());
            }

            // Delete the rejected connection
            rejectedConnectionRepository.deleteById(connectionId);

            return new DeleteRejectedConnectionResults.Success(request.requestId(),
                    request.tracingId());

        } catch (Exception e) {
            return new DeleteRejectedConnectionResults.SystemError(request.requestId(),
                    "Failed to delete rejected connection: " + e.getMessage(), request.tracingId());
        }
    }
}
