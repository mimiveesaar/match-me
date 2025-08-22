package tech.kood.match_me.connections.features.declineConnection;

public record DeclineConnectionEvent(DeclineConnectionRequest request,
                                     DeclineConnectionResults results) {
}
