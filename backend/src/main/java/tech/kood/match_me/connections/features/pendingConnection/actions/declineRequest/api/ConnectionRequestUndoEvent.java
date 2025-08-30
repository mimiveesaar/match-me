package tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;


/**
 * User took back their connection request.
 * @param connectionIdDTO
 * @param senderId
 * @param targetId
 */
public record ConnectionRequestUndoEvent(
        @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
        @NotNull @Valid @JsonProperty("sender_id") UserIdDTO senderId,
        @NotNull @Valid @JsonProperty("target_id") UserIdDTO targetId
) {
}
