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
    uniqueConstraints = @UniqueConstraint(columnNames = {"rejecter_id", "rejected_id"})
)
public class UserRejection {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "rejecter_id", nullable = false)
    private UUID rejecterId;

    @Column(name = "rejected_id", nullable = false)
    private UUID rejectedId;

    protected UserRejection() {}

    public UserRejection(UUID rejecterId, UUID rejectedId) {
        this.rejecterId = rejecterId;
        this.rejectedId = rejectedId;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getRejecterId() { return rejecterId; }
    public UUID getRejectedId() { return rejectedId; }

    //Setters
    public void setId(UUID id) { this.id = id; }
    public void setRejecterId(UUID rejecterId) { this.rejecterId = rejecterId; }
    public void setRejectedId(UUID rejectedId) { this.rejectedId = rejectedId; }
}