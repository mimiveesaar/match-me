package tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;

public sealed interface ConnectionRequestResults
        permits ConnectionRequestResults.Success, ConnectionRequestResults.AlreadyExists,
        ConnectionRequestResults.SystemError, ConnectionRequestResults.InvalidRequest {

    record Success(@NotNull @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO)
            implements ConnectionRequestResults {
    }

    record AlreadyExists() implements ConnectionRequestResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
            implements ConnectionRequestResults {
    }

    record SystemError(@NotEmpty @JsonProperty("message") String message)
            implements ConnectionRequestResults {
    }
}
