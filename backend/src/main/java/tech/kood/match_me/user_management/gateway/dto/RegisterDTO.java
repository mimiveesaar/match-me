package tech.kood.match_me.user_management.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.common.domain.api.PasswordDTO;

public record RegisterDTO(@Valid @NotNull @JsonProperty("email") EmailDTO email, @Valid @NotNull @JsonProperty("password") PasswordDTO password) {}
