package tech.kood.match_me.connections.features.rejectedConnection.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.rejectedConnection.actions.GetRejectionBetweenUsers;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionDTO;
import tech.kood.match_me.connections.features.rejectedConnection.internal.mapper.RejectedConnectionMapper;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity.RejectedConnectionEntity;

import java.util.Optional;

@Component
@ApplicationLayer
public class GetRejectionBetweenUsersHandlerImpl
        implements GetRejectionBetweenUsers.Handler {

    private final RejectedConnectionRepository rejectedConnectionRepository;
    private final RejectedConnectionMapper rejectedConnectionMapper;
    private final UserIdFactory userIdFactory;
    private final Validator validator;

    public GetRejectionBetweenUsersHandlerImpl(
            RejectedConnectionRepository rejectedConnectionRepository,
            RejectedConnectionMapper rejectedConnectionMapper, UserIdFactory userIdFactory,
            Validator validator) {
        this.rejectedConnectionRepository = rejectedConnectionRepository;
        this.rejectedConnectionMapper = rejectedConnectionMapper;
        this.userIdFactory = userIdFactory;
        this.validator = validator;
    }

    @Override
    public GetRejectionBetweenUsers.Result handle(GetRejectionBetweenUsers.Request request) {
        var validationErrors = validator.validate(request);

        if (!validationErrors.isEmpty()) {
            return new GetRejectionBetweenUsers.Result.InvalidRequest(
                    InvalidInputErrorDTO.fromValidation(validationErrors));
        }

        try {
            var user1Id = userIdFactory.create(request.user1().value());
            var user2Id = userIdFactory.create(request.user2().value());

            Optional<RejectedConnectionEntity> rejectionEntity =
                    rejectedConnectionRepository.findBetweenUsers(user1Id.getValue(),
                            user2Id.getValue());

            if (rejectionEntity.isEmpty()) {
                return new GetRejectionBetweenUsers.Result.NotFound();
            }

            RejectedConnectionDTO rejectionDTO;
            try {
                rejectionDTO = rejectedConnectionMapper.toDTO(rejectionEntity.get());
            } catch (CheckedConstraintViolationException e) {
                throw new RuntimeException("Failed to map rejected connection entity to DTO", e);
            }

            return new GetRejectionBetweenUsers.Result.Success(rejectionDTO);

        } catch (Exception e) {
            return new GetRejectionBetweenUsers.Result.SystemError(
                    "Failed to get rejection between users: " + e.getMessage());
        }
    }
}
