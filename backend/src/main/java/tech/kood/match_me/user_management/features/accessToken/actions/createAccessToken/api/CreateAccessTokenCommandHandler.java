package tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api;

import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.transaction.annotation.Transactional;

@ApplicationLayer
public interface CreateAccessTokenCommandHandler {
    @Transactional
    CreateAccessTokenResults handle(CreateAccessTokenRequest request);
}
