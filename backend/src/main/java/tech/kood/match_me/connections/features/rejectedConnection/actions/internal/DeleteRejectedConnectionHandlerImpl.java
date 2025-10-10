package tech.kood.match_me.connections.features.rejectedConnection.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.rejectedConnection.actions.DeleteRejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;

@Component
@ApplicationLayer
public class DeleteRejectedConnectionHandlerImpl
        implements DeleteRejectedConnection.Handler {

    private final RejectedConnectionRepository rejectedConnectionRepository;
    private final Validator validator;

    public DeleteRejectedConnectionHandlerImpl(
            RejectedConnectionRepository rejectedConnectionRepository, Validator validator) {
        this.rejectedConnectionRepository = rejectedConnectionRepository;
        this.validator = validator;
    }

    @Override
    @Transactional
    public DeleteRejectedConnection.Result handle(DeleteRejectedConnection.Request request) {

        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            return new DeleteRejectedConnection.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            var entityOptional = rejectedConnectionRepository.findById(request.connectionIdDTO().value());

            if (entityOptional.isEmpty()) {
                return new DeleteRejectedConnection.Result.NotFound();
            }

            var entity = entityOptional.get();
            boolean deleted = rejectedConnectionRepository.deleteById(entity.getId());

            return new DeleteRejectedConnection.Result.Success();

        } catch (Exception e) {
            return new DeleteRejectedConnection.Result.SystemError(
                    "Failed to delete rejected connection: " + e.getMessage());
        }
    }
}
