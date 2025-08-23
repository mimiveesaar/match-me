package tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api;

import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public interface CreateRejectedConnectionCommandHandler {
    CreateRejectedConnectionResults handle(CreateRejectedConnection request);
}
