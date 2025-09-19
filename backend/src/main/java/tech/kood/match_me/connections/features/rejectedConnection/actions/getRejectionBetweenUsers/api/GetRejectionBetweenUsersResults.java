package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionDTO;

public sealed interface GetRejectionBetweenUsersResults
        permits GetRejectionBetweenUsersResults.Success, GetRejectionBetweenUsersResults.NotFound,
        GetRejectionBetweenUsersResults.SystemError,
        GetRejectionBetweenUsersResults.InvalidRequest {

    record Success(@NotNull @Valid @JsonProperty("rejection") RejectedConnectionDTO rejection)
            implements GetRejectionBetweenUsersResults {
    }

    record NotFound()
            implements GetRejectionBetweenUsersResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
            implements GetRejectionBetweenUsersResults {
    }

    record SystemError(@NotEmpty @JsonProperty("message") String message)
            implements GetRejectionBetweenUsersResults {
    }
}
