package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectedUsersByUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionDTO;

import java.util.List;

public sealed interface GetRejectedUsersByUserResults permits GetRejectedUsersByUserResults.Success,
        GetRejectedUsersByUserResults.SystemError, GetRejectedUsersByUserResults.InvalidRequest {

    record Success(@NotNull @JsonProperty("rejections") List<@Valid RejectedConnectionDTO> rejections)
            implements GetRejectedUsersByUserResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
            implements GetRejectedUsersByUserResults {
    }

    record SystemError(@NotEmpty @JsonProperty("message") String message)
            implements GetRejectedUsersByUserResults {
    }
}
