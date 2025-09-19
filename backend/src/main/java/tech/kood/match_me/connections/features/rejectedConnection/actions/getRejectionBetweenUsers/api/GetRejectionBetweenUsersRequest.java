package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api.validation.ValidGetRejectionBetweenUsers;

import java.io.Serializable;

@ValidGetRejectionBetweenUsers
public record GetRejectionBetweenUsersRequest(@NotNull @Valid @JsonProperty("user1") UserIdDTO user1,
                                              @NotNull @Valid @JsonProperty("user2") UserIdDTO user2) implements Serializable {

    public GetRejectionBetweenUsersRequest withUser1(UserIdDTO user1) {
        return new GetRejectionBetweenUsersRequest(user1, user2);
    }

    public GetRejectionBetweenUsersRequest withUser2(UserIdDTO user2) {
        return new GetRejectionBetweenUsersRequest(user1, user2);
    }
}
