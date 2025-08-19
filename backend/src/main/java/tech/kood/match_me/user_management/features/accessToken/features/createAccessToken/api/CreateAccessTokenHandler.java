package tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.api;

import jakarta.transaction.Transactional;
import org.jmolecules.architecture.cqrs.CommandHandler;

public interface CreateAccessTokenHandler {
    @Transactional
    CreateAccessTokenResults handle(CreateAccessTokenRequest request);
}
