package tech.kood.match_me.user_management.common.domain.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record AccessTokenDTO(@NotBlank @JsonProperty("jwt") String jwt) {
}
