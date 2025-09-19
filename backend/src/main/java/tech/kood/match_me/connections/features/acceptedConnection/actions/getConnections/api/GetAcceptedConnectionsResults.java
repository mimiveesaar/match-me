package tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.acceptedConnection.domain.api.AcceptedConnectionDTO;

import java.util.List;

public sealed interface GetAcceptedConnectionsResults permits
        GetAcceptedConnectionsResults.Success,
        GetAcceptedConnectionsResults.InvalidRequest,
        GetAcceptedConnectionsResults.SystemError {
    
    record Success(
            @NotNull @JsonProperty("accepted_connections") List<AcceptedConnectionDTO> acceptedConnections) implements GetAcceptedConnectionsResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error
    ) implements GetAcceptedConnectionsResults {
    }

    record SystemError(@NotEmpty @JsonProperty("message") String message) implements GetAcceptedConnectionsResults {
    }
}
