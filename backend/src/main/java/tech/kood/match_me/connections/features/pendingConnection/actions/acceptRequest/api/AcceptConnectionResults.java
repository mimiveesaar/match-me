package tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;

public sealed interface AcceptConnectionResults permits AcceptConnectionResults.Success,
        AcceptConnectionResults.NotFound, AcceptConnectionResults.InvalidRequest,
        AcceptConnectionResults.AlreadyAccepted, AcceptConnectionResults.SystemError {

    record Success() implements AcceptConnectionResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
            implements AcceptConnectionResults {
    }

    record NotFound() implements AcceptConnectionResults {
    }

    record AlreadyAccepted() implements AcceptConnectionResults {
    }

    record SystemError(@NotEmpty String message) implements AcceptConnectionResults {
    }
}
