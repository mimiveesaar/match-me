package tech.kood.match_me.user_management.features.user.actions.getUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.QueryModel;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.common.domain.api.UserIdDTO;

@QueryModel
@ApplicationLayer
public record GetUserByIdRequest(@NotNull @Valid @JsonProperty("user_id") UserIdDTO userId) {
}
