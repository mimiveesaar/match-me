package tech.kood.match_me.connections.features.acceptedConnection.actions.createAcceptedConnection.api;

import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public interface CreateAcceptedConnectionCommandHandler {
    CreateAcceptedConnectionResults handle(CreateAcceptedConnectionRequest request);
}
