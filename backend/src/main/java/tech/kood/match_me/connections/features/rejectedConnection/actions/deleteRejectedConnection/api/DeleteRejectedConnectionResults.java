package tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;

public sealed interface DeleteRejectedConnectionResults
        permits DeleteRejectedConnectionResults.Success, DeleteRejectedConnectionResults.NotFound,
        DeleteRejectedConnectionResults.AlreadyDeleted,
        DeleteRejectedConnectionResults.InvalidRequest,
        DeleteRejectedConnectionResults.SystemError {

    record Success()
            implements DeleteRejectedConnectionResults {
    }

    record NotFound()
            implements DeleteRejectedConnectionResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
            implements DeleteRejectedConnectionResults {
    }

    record AlreadyDeleted()
            implements DeleteRejectedConnectionResults {
    }

    record SystemError(@NotEmpty String message)
            implements DeleteRejectedConnectionResults {
    }
}
