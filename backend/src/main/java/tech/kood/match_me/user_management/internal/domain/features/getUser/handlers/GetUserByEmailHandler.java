package tech.kood.match_me.user_management.internal.domain.features.getUser.handlers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tech.kood.match_me.user_management.internal.common.cqrs.QueryHandler;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.domain.features.getUser.events.GetUserByEmailEvent;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByEmailQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByEmailQueryResults;
import tech.kood.match_me.user_management.internal.mappers.UserMapper;

@Service
public class GetUserByEmailHandler
        implements QueryHandler<GetUserByEmailQuery, GetUserByEmailQueryResults> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher events;

    public GetUserByEmailHandler(UserRepository userRepository, UserMapper userMapper,
            ApplicationEventPublisher events) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.events = events;
    }

    public GetUserByEmailQueryResults handle(GetUserByEmailQuery request) {
        String email = request.email;

        try {
            var userQueryResult = userRepository.findUserByEmail(email);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var user = userMapper.toUser(userEntity);

                var result = GetUserByEmailQueryResults.Success.of(user, request.requestId,
                        request.tracingId);
                events.publishEvent(new GetUserByEmailEvent(request, result));
                return result;
            } else {
                var result = GetUserByEmailQueryResults.UserNotFound.of(email, request.requestId,
                        request.tracingId);
                events.publishEvent(new GetUserByEmailEvent(request, result));
                return result;
            }
        } catch (Exception e) {
            var result = GetUserByEmailQueryResults.SystemError.of(
                    "Failed to retrieve user by email: " + e.getMessage(), request.requestId,
                    request.tracingId);
            events.publishEvent(new GetUserByEmailEvent(request, result));
            return result;
        }
    }
}
