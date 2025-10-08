package tech.kood.match_me.matching.events;

import java.util.UUID;

public class UserConnectionRequestEvent {
    private final UUID requesterId;
    private final UUID requestedId;

    public UserConnectionRequestEvent(UUID requesterId, UUID requestedId) {
        this.requesterId = requesterId;
        this.requestedId = requestedId;
    }

    public UUID getRequesterId() { return  requesterId; }
    public UUID getRequestedId() { return  requestedId; }
}

// so we might want a dto here instead, cause this is looking more like the interface i was supposed to make and what
// i actually want to send is both requester and requested ids at once and with an id for the event too.