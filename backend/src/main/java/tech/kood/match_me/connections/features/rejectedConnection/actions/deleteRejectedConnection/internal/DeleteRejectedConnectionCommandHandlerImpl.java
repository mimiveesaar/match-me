package tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.internal;

import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api.DeleteRejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api.DeleteRejectedConnectionCommandHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api.DeleteRejectedConnectionResults;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;

import java.util.UUID;

@Component
@ApplicationLayer
public class DeleteRejectedConnectionCommandHandlerImpl
        implements DeleteRejectedConnectionCommandHandler {

    private final RejectedConnectionRepository rejectedConnectionRepository;

    public DeleteRejectedConnectionCommandHandlerImpl(
            RejectedConnectionRepository rejectedConnectionRepository) {
        this.rejectedConnectionRepository = rejectedConnectionRepository;
    }

    @Override
    @Transactional
    public DeleteRejectedConnectionResults handle(DeleteRejectedConnection request) {
        try {
            UUID connectionId = request.connectionId().value();

            // Check if the rejected connection exists
            var existingRejection = rejectedConnectionRepository.findById(connectionId);

            if (existingRejection.isEmpty()) {
                return new DeleteRejectedConnectionResults.NotFound(request.requestId(),
                        request.tracingId());
            }

            // Delete the rejected connection
            boolean deleted = rejectedConnectionRepository.deleteById(connectionId);

            if (deleted) {
                return new DeleteRejectedConnectionResults.Success(request.requestId(),
                        request.tracingId());
            } else {
                return new DeleteRejectedConnectionResults.AlreadyDeleted(request.requestId(),
                        request.tracingId());
            }

        } catch (Exception e) {
            return new DeleteRejectedConnectionResults.SystemError(request.requestId(),
                    "Failed to delete rejected connection: " + e.getMessage(), request.tracingId());
        }
    }
}
