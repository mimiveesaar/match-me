package tech.kood.match_me.user_management.internal.features.getUser.handlers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tech.kood.match_me.user_management.internal.common.cqrs.QueryHandler;
import tech.kood.match_me.user_management.internal.features.user.persistance.UserRepository;
import tech.kood.match_me.user_management.internal.features.getUser.events.GetUserByIdEvent;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdQueryResults;
import tech.kood.match_me.user_management.internal.mappers.UserMapper;

@Service
public class GetUserByIdHandler implements QueryHandler<GetUserByIdQuery, GetUserByIdQueryResults> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher events;

    public GetUserByIdHandler(UserRepository userRepository, UserMapper userMapper,
            ApplicationEventPublisher events) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.events = events;
    }

    public GetUserByIdQueryResults handle(GetUserByIdQuery request) {
        try {
            var userQueryResult = userRepository.findUserById(request.getUserId().getValue());
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var user = userMapper.toUser(userEntity);

                var result = GetUserByIdQueryResults.Success.of(user, request.getRequestId(),
                        request.getTracingId());
                events.publishEvent(new GetUserByIdEvent(request, result));
                return result;
            } else {
                var result = GetUserByIdQueryResults.UserNotFound.of(request.getUserId(),
                        request.getRequestId(), request.getTracingId());
                events.publishEvent(new GetUserByIdEvent(request, result));
                return result;
            }
        } catch (Exception e) {
            var result = GetUserByIdQueryResults.SystemError.of(
                    "Failed to retrieve user by ID: " + e.getMessage(), request.getRequestId(),
                    request.getTracingId());
            events.publishEvent(new GetUserByIdEvent(request, result));
            return result;
        }
    }
}
