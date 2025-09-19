package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionsByUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.io.Serializable;

public record GetRejectionsByUserRequest(@NotNull @Valid @JsonProperty("rejected_user") UserIdDTO rejectedUser) implements Serializable {

    public GetRejectionsByUserRequest withRejectedUser(UserIdDTO rejectedUser) {
        return new GetRejectionsByUserRequest(rejectedUser);
    }
}
