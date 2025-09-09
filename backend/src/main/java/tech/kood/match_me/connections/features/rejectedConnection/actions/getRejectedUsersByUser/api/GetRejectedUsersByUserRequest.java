package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectedUsersByUser.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;

public record GetRejectedUsersByUserRequest(@NotNull @Valid @JsonProperty("rejected_by_user") UserIdDTO rejectedByUser) implements Serializable {

    public GetRejectedUsersByUserRequest withRejectedByUser(UserIdDTO rejectedByUser) {
        return new GetRejectedUsersByUserRequest(rejectedByUser);
    }
}
