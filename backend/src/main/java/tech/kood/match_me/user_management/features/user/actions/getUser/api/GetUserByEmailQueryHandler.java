package tech.kood.match_me.user_management.features.user.actions.getUser.api;

import jakarta.transaction.Transactional;

public interface GetUserByEmailQueryHandler {
    @Transactional
    GetUserByEmailResults handle(GetUserByEmailRequest request);
}
