package tech.kood.match_me.matching.events;

import java.util.UUID;

public class UserRejectionRequestEvent {
    private final UUID requesterId;
    private final UUID requestedId;

    public UserRejectionRequestEvent(UUID requesterId, UUID requestedId) {
        this.requesterId = requesterId;
        this.requestedId = requestedId;
    }

    public UUID getRequesterId() { return requesterId; }
    public UUID getRequestedId() { return requestedId; }
}
