package tech.kood.match_me.user_management.features.user.actions.getUser.api;


import org.springframework.transaction.annotation.Transactional;

public interface GetUserByIdQueryHandler {
    @Transactional
    GetUserByIdResults handle(GetUserByIdRequest request);
}
