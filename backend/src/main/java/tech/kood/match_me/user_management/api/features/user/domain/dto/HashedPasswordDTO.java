package tech.kood.match_me.user_management.api.features.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public record HashedPasswordDTO(
        @JsonProperty("hash") @NotBlank String hash,
        @JsonProperty("salt") @NotBlank String salt
) {
}
