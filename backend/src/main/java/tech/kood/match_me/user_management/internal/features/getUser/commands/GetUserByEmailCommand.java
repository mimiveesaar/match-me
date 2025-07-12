package tech.kood.match_me.user_management.internal.features.getUser.commands;

import tech.kood.match_me.user_management.internal.common.Command;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByEmailRequest;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByEmailResults;

public class GetUserByEmailCommand extends Command<GetUserByEmailRequest, GetUserByEmailResults> {

    public GetUserByEmailCommand(GetUserByEmailRequest request) {
        super(request);
    }
}