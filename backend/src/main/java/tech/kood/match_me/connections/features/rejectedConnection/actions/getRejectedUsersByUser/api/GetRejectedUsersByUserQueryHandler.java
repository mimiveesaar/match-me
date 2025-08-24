package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectedUsersByUser.api;

import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public interface GetRejectedUsersByUserQueryHandler {
    GetRejectedUsersByUserResults handle(GetRejectedUsersByUser request);
}
