package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api.GetRejectionBetweenUsersRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api.GetRejectionBetweenUsersQueryHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api.GetRejectionBetweenUsersResults;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionDTO;
import tech.kood.match_me.connections.features.rejectedConnection.internal.mapper.RejectedConnectionMapper;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity.RejectedConnectionEntity;

import java.util.Optional;

@Component
@ApplicationLayer
public class GetRejectionBetweenUsersQueryHandlerImpl
        implements GetRejectionBetweenUsersQueryHandler {

    private final RejectedConnectionRepository rejectedConnectionRepository;
    private final RejectedConnectionMapper rejectedConnectionMapper;
    private final UserIdFactory userIdFactory;
    private final Validator validator;

    public GetRejectionBetweenUsersQueryHandlerImpl(
            RejectedConnectionRepository rejectedConnectionRepository,
            RejectedConnectionMapper rejectedConnectionMapper, UserIdFactory userIdFactory,
            Validator validator) {
        this.rejectedConnectionRepository = rejectedConnectionRepository;
        this.rejectedConnectionMapper = rejectedConnectionMapper;
        this.userIdFactory = userIdFactory;
        this.validator = validator;
    }

    @Override
    public GetRejectionBetweenUsersResults handle(GetRejectionBetweenUsersRequest request) {
        var validationErrors = validator.validate(request);

        if (!validationErrors.isEmpty()) {
            return new GetRejectionBetweenUsersResults.InvalidRequest(request.requestId(),
                    InvalidInputErrorDTO.fromValidation(validationErrors), request.tracingId());
        }

        try {
            var user1Id = userIdFactory.create(request.user1().value());
            var user2Id = userIdFactory.create(request.user2().value());

            Optional<RejectedConnectionEntity> rejectionEntity =
                    rejectedConnectionRepository.findBetweenUsers(user1Id.getValue(),
                            user2Id.getValue());

            if (rejectionEntity.isEmpty()) {
                return new GetRejectionBetweenUsersResults.NotFound(request.requestId(),
                        request.tracingId());
            }

            RejectedConnectionDTO rejectionDTO;
            try {
                rejectionDTO = rejectedConnectionMapper.toDTO(rejectionEntity.get());
            } catch (CheckedConstraintViolationException e) {
                throw new RuntimeException("Failed to map rejected connection entity to DTO", e);
            }

            return new GetRejectionBetweenUsersResults.Success(request.requestId(), rejectionDTO,
                    request.tracingId());

        } catch (Exception e) {
            return new GetRejectionBetweenUsersResults.SystemError(request.requestId(),
                    "Failed to get rejection between users: " + e.getMessage(),
                    request.tracingId());
        }
    }
}
