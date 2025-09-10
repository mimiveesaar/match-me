package tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.api;

public interface GetPendingConnectionsQueryHandler {
    GetPendingConnectionsResults handle(GetPendingConnectionsRequest request);
}
