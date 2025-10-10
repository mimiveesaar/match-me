package tech.kood.match_me.connections.features.rejectedConnection.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.rejectedConnection.actions.GetRejectedUsersByUser;
import tech.kood.match_me.connections.features.rejectedConnection.internal.mapper.RejectedConnectionMapper;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;

import java.util.stream.Collectors;

@Component
@ApplicationLayer
public class GetRejectedUsersByUserHandlerImpl
        implements GetRejectedUsersByUser.Handler {

    private final RejectedConnectionRepository rejectedConnectionRepository;
    private final RejectedConnectionMapper rejectedConnectionMapper;
    private final UserIdFactory userIdFactory;
    private final Validator validator;

    public GetRejectedUsersByUserHandlerImpl(
            RejectedConnectionRepository rejectedConnectionRepository,
            RejectedConnectionMapper rejectedConnectionMapper, UserIdFactory userIdFactory,
            Validator validator) {
        this.rejectedConnectionRepository = rejectedConnectionRepository;
        this.rejectedConnectionMapper = rejectedConnectionMapper;
        this.userIdFactory = userIdFactory;
        this.validator = validator;
    }

    @Override
    public GetRejectedUsersByUser.Result handle(GetRejectedUsersByUser.Request request) {
        var validationErrors = validator.validate(request);
        if (!validationErrors.isEmpty()) {
            return new GetRejectedUsersByUser.Result.InvalidRequest(
                    InvalidInputErrorDTO.fromValidation(validationErrors));
        }

        try {
            var rejectedByUserId = userIdFactory.create(request.rejectedByUser().value());

            var rejectedConnections =
                    rejectedConnectionRepository.findByRejectedByUser(rejectedByUserId.getValue());

            var rejectionDTOs = rejectedConnections.stream().map(entity -> {
                try {
                    return rejectedConnectionMapper.toDTO(entity);
                } catch (CheckedConstraintViolationException e) {
                    throw new RuntimeException("Failed to map rejected connection entity to DTO",
                            e);
                }
            }).collect(Collectors.toList());

            return new GetRejectedUsersByUser.Result.Success(rejectionDTOs);

        } catch (Exception e) {
            return new GetRejectedUsersByUser.Result.SystemError(
                    "Failed to get rejected users by user: " + e.getMessage());
        }
    }
}
