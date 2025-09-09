package tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.pendingConnection.domain.api.PendingConnectionDTO;

import java.util.List;

public sealed interface GetIncomingConnectionsResults permits
        GetIncomingConnectionsResults.Success,
        GetIncomingConnectionsResults.InvalidRequest,
        GetIncomingConnectionsResults.SystemError {
    record Success(
            @NotNull @JsonProperty("incoming_requests") List<PendingConnectionDTO> incomingRequests) implements GetIncomingConnectionsResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error
    ) implements GetIncomingConnectionsResults {
    }

    record SystemError(@NotEmpty String message) implements GetIncomingConnectionsResults {
    }
}