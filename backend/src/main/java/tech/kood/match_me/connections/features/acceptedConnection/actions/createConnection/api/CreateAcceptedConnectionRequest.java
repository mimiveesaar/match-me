package tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;

@Command
public record CreateAcceptedConnectionRequest(@NotNull @JsonProperty("request_id") UUID requestId,
        @NotNull @Valid @JsonProperty("accepted_by_user") UserIdDTO acceptedByUser,
        @NotNull @Valid @JsonProperty("accepted_user") UserIdDTO acceptedUser,
        @Nullable @JsonProperty("tracing_id") String tracingId) implements Serializable {

    public CreateAcceptedConnectionRequest(UserIdDTO acceptedByUser, UserIdDTO acceptedUser,
            @Nullable String tracingId) {
        this(UUID.randomUUID(), acceptedByUser, acceptedUser, tracingId);
    }

    public CreateAcceptedConnectionRequest withRequestId(UUID requestId) {
        return new CreateAcceptedConnectionRequest(requestId, acceptedByUser, acceptedUser,
                tracingId);
    }

    public CreateAcceptedConnectionRequest withAcceptedByUser(UserIdDTO acceptedByUser) {
        return new CreateAcceptedConnectionRequest(requestId, acceptedByUser, acceptedUser,
                tracingId);
    }

    public CreateAcceptedConnectionRequest withAcceptedUser(UserIdDTO acceptedUser) {
        return new CreateAcceptedConnectionRequest(requestId, acceptedByUser, acceptedUser,
                tracingId);
    }

    public CreateAcceptedConnectionRequest withTracingId(String tracingId) {
        return new CreateAcceptedConnectionRequest(requestId, acceptedByUser, acceptedUser,
                tracingId);
    }
}
