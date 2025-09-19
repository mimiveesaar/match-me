package tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api;

public interface GetIncomingConnectionQueryHandler {
    GetIncomingConnectionsResults handle(GetIncomingConnectionsRequest request);
}
