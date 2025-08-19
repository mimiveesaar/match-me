package tech.kood.match_me.user_management.features.accessToken.internal.mapper;


import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.features.accessToken.domain.api.AccessTokenDTO;
import tech.kood.match_me.user_management.features.accessToken.domain.internal.model.AccessToken;

@Component
@ApplicationLayer
public final class AccessTokenMapper {

    //It only makes sense to convert to DTO.
    public AccessTokenDTO toDTO(AccessToken accessToken) {
        return new AccessTokenDTO(accessToken.getJwt());
    }
}
