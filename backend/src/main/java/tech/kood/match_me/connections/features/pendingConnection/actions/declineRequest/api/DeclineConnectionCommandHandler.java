package tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api;

public interface DeclineConnectionCommandHandler {
    DeclineConnectionResults handle(DeclineConnectionRequest request);
}
