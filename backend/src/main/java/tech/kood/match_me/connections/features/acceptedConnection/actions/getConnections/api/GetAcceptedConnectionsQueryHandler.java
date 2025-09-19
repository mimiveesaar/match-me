package tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api;

public interface GetAcceptedConnectionsQueryHandler {
    GetAcceptedConnectionsResults handle(GetAcceptedConnectionsRequest request);
}
