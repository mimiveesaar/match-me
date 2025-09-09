package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionsByUser.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionsByUser.api.GetRejectionsByUserRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionsByUser.api.GetRejectionsByUserQueryHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionsByUser.api.GetRejectionsByUserResults;
import tech.kood.match_me.connections.features.rejectedConnection.internal.mapper.RejectedConnectionMapper;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;

import java.util.stream.Collectors;

import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;

@Component
@ApplicationLayer
public class GetRejectionsByUserQueryHandlerImpl implements GetRejectionsByUserQueryHandler {

    private final RejectedConnectionRepository rejectedConnectionRepository;
    private final RejectedConnectionMapper rejectedConnectionMapper;
    private final UserIdFactory userIdFactory;
    private final Validator validator;

    public GetRejectionsByUserQueryHandlerImpl(
            RejectedConnectionRepository rejectedConnectionRepository,
            RejectedConnectionMapper rejectedConnectionMapper, UserIdFactory userIdFactory,
            Validator validator) {
        this.rejectedConnectionRepository = rejectedConnectionRepository;
        this.rejectedConnectionMapper = rejectedConnectionMapper;
        this.userIdFactory = userIdFactory;
        this.validator = validator;
    }

    @Override
    public GetRejectionsByUserResults handle(GetRejectionsByUserRequest request) {

        var validationErrors = validator.validate(request);
        if (!validationErrors.isEmpty()) {
            return new GetRejectionsByUserResults.InvalidRequest(request.requestId(),
                    InvalidInputErrorDTO.fromValidation(validationErrors), request.tracingId());
        }

        try {
            var rejectedUserId = userIdFactory.create(request.rejectedUser().value());

            var rejectedConnections = rejectedConnectionRepository.findByRejectedUser(rejectedUserId.getValue());

            var rejectionDTOs = rejectedConnections.stream().map(entity -> {
                try {
                    return rejectedConnectionMapper.toDTO(entity);
                } catch (CheckedConstraintViolationException e) {
                    throw new RuntimeException("Failed to map rejected connection entity to DTO",
                            e);
                }
            }).collect(Collectors.toList());

            return new GetRejectionsByUserResults.Success(request.requestId(), rejectionDTOs,
                    request.tracingId());

        } catch (Exception e) {
            return new GetRejectionsByUserResults.SystemError(request.requestId(),
                    "Failed to get rejections by user: " + e.getMessage(), request.tracingId());
        }
    }
}
