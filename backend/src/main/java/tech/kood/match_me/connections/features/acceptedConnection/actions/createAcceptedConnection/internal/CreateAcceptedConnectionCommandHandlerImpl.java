package tech.kood.match_me.connections.features.acceptedConnection.actions.createAcceptedConnection.internal;

import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Service;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createAcceptedConnection.api.CreateAcceptedConnectionCommandHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createAcceptedConnection.api.CreateAcceptedConnectionRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createAcceptedConnection.api.CreateAcceptedConnectionResults;

@Service
@ApplicationLayer
public class CreateAcceptedConnectionCommandHandlerImpl
        implements CreateAcceptedConnectionCommandHandler {

    @Override
    public CreateAcceptedConnectionResults handle(CreateAcceptedConnectionRequest request) {
        // TODO: Implement the logic to create an accepted connection
        // This should include validation, persistence, and returning appropriate results
        return new CreateAcceptedConnectionResults.SystemError(request.requestId(),
                "Not implemented yet", request.tracingId());
    }
}
