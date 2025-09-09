package tech.kood.match_me.user_management.features.accessToken.actions.validateAccessToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;


public record ValidateAccessTokenRequest(@NotBlank @JsonProperty("jwt") String jwtToken) {
}