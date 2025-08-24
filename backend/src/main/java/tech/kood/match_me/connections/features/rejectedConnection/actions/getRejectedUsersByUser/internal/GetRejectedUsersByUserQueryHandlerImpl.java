package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectedUsersByUser.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectedUsersByUser.api.GetRejectedUsersByUser;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectedUsersByUser.api.GetRejectedUsersByUserQueryHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectedUsersByUser.api.GetRejectedUsersByUserResults;
import tech.kood.match_me.connections.features.rejectedConnection.internal.mapper.RejectedConnectionMapper;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;

import java.util.stream.Collectors;

@Component
@ApplicationLayer
public class GetRejectedUsersByUserQueryHandlerImpl
        implements GetRejectedUsersByUserQueryHandler {

    private final RejectedConnectionRepository rejectedConnectionRepository;
    private final RejectedConnectionMapper rejectedConnectionMapper;
    private final UserIdFactory userIdFactory;
    private final Validator validator;

    public GetRejectedUsersByUserQueryHandlerImpl(
            RejectedConnectionRepository rejectedConnectionRepository,
            RejectedConnectionMapper rejectedConnectionMapper, UserIdFactory userIdFactory,
            Validator validator) {
        this.rejectedConnectionRepository = rejectedConnectionRepository;
        this.rejectedConnectionMapper = rejectedConnectionMapper;
        this.userIdFactory = userIdFactory;
        this.validator = validator;
    }

    @Override
    public GetRejectedUsersByUserResults handle(GetRejectedUsersByUser request) {
        var validationErrors = validator.validate(request);
        if (!validationErrors.isEmpty()) {
            return new GetRejectedUsersByUserResults.InvalidRequest(request.requestId(),
                    InvalidInputErrorDTO.from(validationErrors), request.tracingId());
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

            return new GetRejectedUsersByUserResults.Success(request.requestId(), rejectionDTOs,
                    request.tracingId());

        } catch (Exception e) {
            return new GetRejectedUsersByUserResults.SystemError(request.requestId(),
                    "Failed to get rejected users by user: " + e.getMessage(), request.tracingId());
        }
    }
}
