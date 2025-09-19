package tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api;

public interface AcceptConnectionCommandHandler {
    AcceptConnectionResults handle(AcceptConnectionRequest request);
}
