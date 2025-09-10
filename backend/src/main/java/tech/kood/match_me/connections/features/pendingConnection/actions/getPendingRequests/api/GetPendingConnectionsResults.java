package tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.pendingConnection.domain.api.PendingConnectionDTO;

import java.util.List;

public sealed interface GetPendingConnectionsResults permits
        GetPendingConnectionsResults.Success,
        GetPendingConnectionsResults.InvalidRequest,
        GetPendingConnectionsResults.SystemError {
    record Success(
            @NotNull @JsonProperty("incoming_requests") List<PendingConnectionDTO> incomingRequests,
            @NotNull @JsonProperty("outgoing_requests") List<PendingConnectionDTO> outgoingRequests
    ) implements GetPendingConnectionsResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error
    ) implements GetPendingConnectionsResults {
    }

    record SystemError(@NotEmpty String message) implements GetPendingConnectionsResults {
    }
}
