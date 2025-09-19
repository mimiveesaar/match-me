package tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;

public sealed interface RejectAcceptedConnectionResults
        permits RejectAcceptedConnectionResults.Success, RejectAcceptedConnectionResults.NotFound,
        RejectAcceptedConnectionResults.AlreadyRejected,
        RejectAcceptedConnectionResults.InvalidRequest,
        RejectAcceptedConnectionResults.SystemError {

    record Success()
            implements RejectAcceptedConnectionResults {
    }

    record NotFound()
            implements RejectAcceptedConnectionResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
            implements RejectAcceptedConnectionResults {
    }

    record AlreadyRejected()
            implements RejectAcceptedConnectionResults {
    }

    record SystemError(@NotEmpty String message)
            implements RejectAcceptedConnectionResults {
    }
}
