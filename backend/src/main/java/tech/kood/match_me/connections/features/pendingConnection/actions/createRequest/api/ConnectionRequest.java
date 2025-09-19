package tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.io.Serializable;

@Command
public record ConnectionRequest(@NotNull @Valid @JsonProperty("target_id") UserIdDTO targetId,
                                @NotNull @Valid @JsonProperty("sender_id") UserIdDTO senderId) implements Serializable {

    public ConnectionRequest withTargetUserId(UserIdDTO targetUserId) {
        return new ConnectionRequest(targetUserId, senderId);
    }

    public ConnectionRequest withSenderId(UserIdDTO senderId) {
        return new ConnectionRequest(targetId, senderId);
    }
}