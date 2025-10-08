package tech.kood.match_me.matching.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "user_rejections",
        uniqueConstraints = @UniqueConstraint(columnNames = {"requester_id", "requested_id"})
)
public class RejectionRequest {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "requester_id", nullable = false)
    private UUID requesterId;

    @Column(name = "requested_id", nullable = false)
    private UUID requestedId;

    protected RejectionRequest() {}

    public RejectionRequest(UUID requesterId, UUID requestedId) {
        this.requesterId = requesterId;
        this.requestedId = requestedId;
    }

    public UUID getId() { return id; }
    public UUID getRequesterId() { return requesterId; }
    public UUID getRequestedId() { return requestedId; }

    public void setId(UUID id) { this.id = id; }
    public void setRequesterId(UUID requesterId) { this.requesterId = requesterId; }
    public void setRequestedId(UUID requestedId) { this.requestedId = requestedId; }
}
