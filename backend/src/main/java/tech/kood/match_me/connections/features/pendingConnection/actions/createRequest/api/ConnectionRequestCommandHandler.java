package tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api;


import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public interface ConnectionRequestCommandHandler {
    ConnectionRequestResults handle(ConnectionRequest request);
}
