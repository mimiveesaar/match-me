package tech.kood.match_me.user_management.internal.features.getUser.commands;

import tech.kood.match_me.user_management.internal.common.Command;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByIdRequest;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdResults;

public class GetUserByIdCommand extends Command<GetUserByIdRequest, GetUserByIdResults> {

    public GetUserByIdCommand(GetUserByIdRequest request) {
        super(request);
    }
}