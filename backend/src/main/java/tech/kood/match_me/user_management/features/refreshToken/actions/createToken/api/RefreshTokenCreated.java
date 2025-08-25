package tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;

public record RefreshTokenCreated(@NotNull @Valid RefreshTokenDTO refreshToken) {}
