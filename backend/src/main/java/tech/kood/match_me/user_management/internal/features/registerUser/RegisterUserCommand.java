package tech.kood.match_me.user_management.internal.features.registerUser;

import tech.kood.match_me.user_management.internal.common.Command;

public class RegisterUserCommand extends Command<RegisterUserRequest, RegisterUserResults> {

    public RegisterUserCommand(RegisterUserRequest request) {
        super(request);
    }
}