package tech.kood.match_me.connections.features.pendingConnection.actions.sendRequest.api;

import java.io.Serializable;
import java.util.UUID;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;

public sealed interface ConnectionRequestResults extends Serializable
        permits ConnectionRequestResults.Success, ConnectionRequestResults.AlreadyExists,
        ConnectionRequestResults.SystemError, ConnectionRequestResults.InvalidRequest {

    public record Success(@NotNull UUID requestId, @NotNull ConnectionIdDTO connectionId,
                          @NotNull UserIdDTO targetUserId,
                          @NotNull UserIdDTO senderId, @Nullable String tracingId)
            implements ConnectionRequestResults {
    }

    public record AlreadyExists(@NotNull UUID requestId, @Nullable String tracingId) implements ConnectionRequestResults {
    }

    public record InvalidRequest(@NotNull UUID requestId, @Nullable String tracingId)
            implements ConnectionRequestResults {
    }

    public record SystemError(@NotNull UUID requestId, @NotEmpty String message, @Nullable String tracingId)
            implements ConnectionRequestResults {
    }
}
