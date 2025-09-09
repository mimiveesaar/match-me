package tech.kood.match_me.connections.features.pendingConnection.actions.getOutgoingRequests.api;

public interface GetOutgoingConnectionQueryHandler {
    GetOutgoingConnectionsResults handle(GetOutgoingConnectionsRequest request);
}
