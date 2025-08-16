package tech.kood.match_me.user_management.api.features.user.register;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.api.features.user.domain.dto.EmailDTO;
import tech.kood.match_me.user_management.api.features.user.domain.dto.PasswordDTO;

public record RegisterUserRequestDTO(@NotNull @Valid EmailDTO email,
                @NotNull PasswordDTO password) {
}
