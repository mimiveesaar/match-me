package tech.kood.match_me.connections.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;

public record ConnectionRequestDTO(@NotNull @Valid @JsonProperty("target_id") UserIdDTO targetId) {
}
