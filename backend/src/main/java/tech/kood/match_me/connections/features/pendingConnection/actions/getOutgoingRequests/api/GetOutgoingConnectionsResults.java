package tech.kood.match_me.connections.features.pendingConnection.actions.getOutgoingRequests.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.pendingConnection.domain.api.PendingConnectionDTO;

import java.util.List;

public sealed interface GetOutgoingConnectionsResults permits
        GetOutgoingConnectionsResults.Success,
        GetOutgoingConnectionsResults.InvalidRequest,
        GetOutgoingConnectionsResults.SystemError {
    record Success(
            @NotNull @JsonProperty("outgoing_requests") List<PendingConnectionDTO> outgoingRequests) implements GetOutgoingConnectionsResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error
    ) implements GetOutgoingConnectionsResults {
    }

    record SystemError(@NotEmpty String message) implements GetOutgoingConnectionsResults {
    }
}
