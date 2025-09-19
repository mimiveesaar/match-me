package tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api;

import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public interface RejectAcceptedConnectionCommandHandler {
    RejectAcceptedConnectionResults handle(RejectAcceptedConnectionRequest request);
}
