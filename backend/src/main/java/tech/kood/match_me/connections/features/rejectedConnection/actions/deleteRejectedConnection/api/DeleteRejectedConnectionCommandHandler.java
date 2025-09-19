package tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api;

import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public interface DeleteRejectedConnectionCommandHandler {
    DeleteRejectedConnectionResults handle(DeleteRejectedConnectionRequest request);
}
