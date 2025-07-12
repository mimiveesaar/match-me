package tech.kood.match_me.user_management.internal.features.getUser.commands;

import tech.kood.match_me.user_management.internal.common.Command;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameRequest;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameResults;

public class GetUserByUsernameCommand extends Command<GetUserByUsernameRequest, GetUserByUsernameResults> {

    public GetUserByUsernameCommand(GetUserByUsernameRequest request) {
        super(request);
    }
}