package tech.kood.match_me.user_management.internal.features.getUser.handlers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tech.kood.match_me.user_management.internal.common.cqrs.QueryHandler;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.getUser.events.GetUserByUsernameEvent;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameQuery;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameQueryResults;
import tech.kood.match_me.user_management.internal.mappers.UserMapper;

@Service
public class GetUserByUsernameHandler
        implements QueryHandler<GetUserByUsernameQuery, GetUserByUsernameQueryResults> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher events;

    public GetUserByUsernameHandler(UserRepository userRepository, UserMapper userMapper,
            ApplicationEventPublisher events) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.events = events;
    }

    public GetUserByUsernameQueryResults handle(GetUserByUsernameQuery request) {
        String username = request.getUsername();

        try {
            var userQueryResult = userRepository.findUserByUsername(username);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var user = userMapper.toUser(userEntity);
                var result = GetUserByUsernameQueryResults.Success.of(user, request.getRequestId(),
                        request.getTracingId());
                events.publishEvent(new GetUserByUsernameEvent(request, result));
                return result;
            } else {
                var result = GetUserByUsernameQueryResults.UserNotFound.of(username,
                        request.getRequestId(), request.getTracingId());
                events.publishEvent(new GetUserByUsernameEvent(request, result));
                return result;
            }
        } catch (Exception e) {
            var result = GetUserByUsernameQueryResults.SystemError.of(
                    "Failed to retrieve user by username: " + e.getMessage(),
                    request.getRequestId(), request.getTracingId());
            events.publishEvent(new GetUserByUsernameEvent(request, result));
            return result;
        }
    }
}
