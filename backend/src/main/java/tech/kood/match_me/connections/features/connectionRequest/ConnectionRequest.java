package tech.kood.match_me.connections.features.connectionRequest;

import java.io.Serializable;
import java.util.UUID;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;

public record ConnectionRequest(@NotNull UUID requestId, @NotNull @Valid UserIdDTO targetUserId,
                                @NotNull @Valid UserIdDTO senderId,
                                @Nullable String tracingId) implements Serializable {
}
