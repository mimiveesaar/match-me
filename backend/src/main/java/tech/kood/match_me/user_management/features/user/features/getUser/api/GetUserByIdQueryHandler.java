package tech.kood.match_me.user_management.features.user.features.getUser.api;

import jakarta.transaction.Transactional;

public interface GetUserByIdQueryHandler {
    @Transactional
    GetUserByIdResults handle(GetUserByIdRequest request);
}
