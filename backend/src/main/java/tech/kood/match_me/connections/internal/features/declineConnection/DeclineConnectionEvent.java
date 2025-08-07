package tech.kood.match_me.connections.internal.features.declineConnection;

public record DeclineConnectionEvent(DeclineConnectionRequest request,
        DeclineConnectionResults results) {
}
