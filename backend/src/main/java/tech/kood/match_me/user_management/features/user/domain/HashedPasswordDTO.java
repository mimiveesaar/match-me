package tech.kood.match_me.user_management.features.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public record HashedPasswordDTO(
        @JsonProperty("hash") @NotBlank String hash,
        @JsonProperty("salt") @NotBlank String salt
) {
}
