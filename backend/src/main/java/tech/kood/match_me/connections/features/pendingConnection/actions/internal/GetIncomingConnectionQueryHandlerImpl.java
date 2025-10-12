package tech.kood.match_me.connections.features.pendingConnection.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.pendingConnection.actions.GetIncomingConnectionRequests;
import tech.kood.match_me.connections.features.pendingConnection.internal.mapper.PendingConnectionMapper;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;

@ApplicationLayer
@Service
public class GetIncomingConnectionQueryHandlerImpl implements GetIncomingConnectionRequests.Handler {

    private final Validator validator;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final PendingConnectionMapper pendingConnectionMapper;

    public GetIncomingConnectionQueryHandlerImpl(Validator validator, PendingConnectionRepository pendingConnectionRepository, PendingConnectionMapper pendingConnectionMapper) {
        this.validator = validator;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.pendingConnectionMapper = pendingConnectionMapper;
    }

    @Override
    public GetIncomingConnectionRequests.Result handle(GetIncomingConnectionRequests.Request request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new GetIncomingConnectionRequests.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            var incomingRequests = pendingConnectionRepository.findByTarget(request.userId().value());

            var incomingRequestsMapped = incomingRequests
                    .stream()
                    .map(entity -> {
                        try {
                            return pendingConnectionMapper.toDTO(entity);
                        } catch (CheckedConstraintViolationException e) {
                            throw new RuntimeException("Failed to map pending connection entity to DTO", e);
                        }
                    })
                    .toList();

            return new GetIncomingConnectionRequests.Result.Success(incomingRequestsMapped);
        } catch (Exception e) {
            return new GetIncomingConnectionRequests.Result.SystemError("An unexpected error occurred while processing the request.");
        }
    }
}