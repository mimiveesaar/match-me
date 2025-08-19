package tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.api;

import jakarta.transaction.Transactional;
import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public interface CreateAccessTokenCommandHandler {
    @Transactional
    CreateAccessTokenResults handle(CreateAccessTokenRequest request);
}
