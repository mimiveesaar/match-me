package tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;

public sealed interface DeclineConnectionResults permits
        DeclineConnectionResults.Success, DeclineConnectionResults.NotFound, DeclineConnectionResults.InvalidRequest,
        DeclineConnectionResults.AlreadyDeclined, DeclineConnectionResults.SystemError {


    record Success() implements DeclineConnectionResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error) implements DeclineConnectionResults {
    }

    record NotFound() implements DeclineConnectionResults {
    }

    record AlreadyDeclined() implements DeclineConnectionResults {
    }

    record SystemError(@NotEmpty String message) implements DeclineConnectionResults {
    }
}
