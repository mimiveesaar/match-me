package tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;

public sealed interface CreateRejectedConnectionResults
        permits CreateRejectedConnectionResults.Success,
        CreateRejectedConnectionResults.AlreadyExists, CreateRejectedConnectionResults.SystemError,
        CreateRejectedConnectionResults.InvalidRequest {

    record Success(@NotNull @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO)
            implements CreateRejectedConnectionResults {
    }

    record AlreadyExists()
            implements CreateRejectedConnectionResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
            implements CreateRejectedConnectionResults {
    }

    record SystemError(@NotEmpty @JsonProperty("message") String message)
            implements CreateRejectedConnectionResults {
    }
}
