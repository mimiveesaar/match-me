package tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api.DeleteRejectedConnectionCommandHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api.DeleteRejectedConnectionRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api.DeleteRejectedConnectionResults;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;

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
            return new DeleteRejectedConnectionResults.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            var entityOptional = rejectedConnectionRepository.findById(request.connectionIdDTO().value());

            if (entityOptional.isEmpty()) {
                return new DeleteRejectedConnectionResults.NotFound();
            }

            var entity = entityOptional.get();
            boolean deleted = rejectedConnectionRepository.deleteById(entity.getId());

            return new DeleteRejectedConnectionResults.Success();

        } catch (Exception e) {
            return new DeleteRejectedConnectionResults.SystemError(
                    "Failed to delete rejected connection: " + e.getMessage());
        }
    }
}
