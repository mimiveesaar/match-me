package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api;

import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public interface GetRejectionBetweenUsersQueryHandler {
    GetRejectionBetweenUsersResults handle(GetRejectionBetweenUsersQuery request);
}
