package tech.kood.match_me.matching.dto;

import java.util.UUID;

public class ConnectionRequestDto {

    private final UUID id;
    private final UUID requesterId;
    private final UUID requestedId;

    public ConnectionRequestDto(UUID id, UUID requesterId, UUID requestedId){
        this.id = id;
        this.requesterId = requesterId;
        this.requestedId = requestedId;
    }

    public UUID getId() { return id; }
    public UUID getRequesterId() { return requesterId; }
    public UUID getRequestedId() { return requestedId; }
}
