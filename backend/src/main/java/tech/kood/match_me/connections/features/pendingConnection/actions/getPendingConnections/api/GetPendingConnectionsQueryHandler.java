package tech.kood.match_me.connections.features.pendingConnection.actions.getPendingConnections.api;

public interface GetPendingConnectionsQueryHandler {
    GetPendingConnectionsResults handle(GetPendingConnectionsRequest request);
}
