package tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

public sealed interface CreateAcceptedConnectionResults
        permits CreateAcceptedConnectionResults.Success,
        CreateAcceptedConnectionResults.AlreadyExists, CreateAcceptedConnectionResults.SystemError,
        CreateAcceptedConnectionResults.InvalidRequest {

    record Success(@NotNull @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO)
            implements CreateAcceptedConnectionResults {
    }

    record AlreadyExists()
            implements CreateAcceptedConnectionResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
            implements CreateAcceptedConnectionResults {
    }

    record SystemError(@NotEmpty @JsonProperty("message") String message)
            implements CreateAcceptedConnectionResults {
    }
}
