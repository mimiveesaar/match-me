package tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.internal;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api.GetIncomingConnectionQueryHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api.GetIncomingConnectionsRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api.GetIncomingConnectionsResults;
import tech.kood.match_me.connections.features.pendingConnection.internal.mapper.PendingConnectionMapper;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;

@Service
public class GetIncomingConnectionQueryHandlerImpl implements GetIncomingConnectionQueryHandler {

    private final Validator validator;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final PendingConnectionMapper pendingConnectionMapper;

    public GetIncomingConnectionQueryHandlerImpl(Validator validator, PendingConnectionRepository pendingConnectionRepository, PendingConnectionMapper pendingConnectionMapper) {
        this.validator = validator;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.pendingConnectionMapper = pendingConnectionMapper;
    }

    @Override
    public GetIncomingConnectionsResults handle(GetIncomingConnectionsRequest request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new GetIncomingConnectionsResults.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
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

            return new GetIncomingConnectionsResults.Success(incomingRequestsMapped);
        } catch (Exception e) {
            return new GetIncomingConnectionsResults.SystemError("An unexpected error occurred while processing the request.");
        }
    }
}