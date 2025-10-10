package tech.kood.match_me.connections.features.pendingConnection.actions.internal;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.GetOutgoingRequests;
import tech.kood.match_me.connections.features.pendingConnection.domain.api.PendingConnectionDTO;
import tech.kood.match_me.connections.features.pendingConnection.internal.mapper.PendingConnectionMapper;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;

import java.util.ArrayList;

@Service
public class GetOutgoingConnectionQueryHandlerImpl implements GetOutgoingRequests.Handler {

    private final Validator validator;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final PendingConnectionMapper pendingConnectionMapper;

    public GetOutgoingConnectionQueryHandlerImpl(Validator validator, PendingConnectionRepository pendingConnectionRepository, PendingConnectionMapper pendingConnectionMapper) {
        this.validator = validator;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.pendingConnectionMapper = pendingConnectionMapper;
    }

    @Override
    public GetOutgoingRequests.Result handle(GetOutgoingRequests.Request request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new GetOutgoingRequests.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            var outgoingRequests = pendingConnectionRepository.findBySender(request.userId().value());
            var outgoingRequestsMapped = new ArrayList<PendingConnectionDTO>();
            for (PendingConnectionEntity outgoingRequest : outgoingRequests) {
                PendingConnectionDTO dto = pendingConnectionMapper.toDTO(outgoingRequest);
                outgoingRequestsMapped.add(dto);
            }

            return new GetOutgoingRequests.Result.Success(outgoingRequestsMapped);
        } catch (Exception e) {
            return new GetOutgoingRequests.Result.SystemError("An unexpected error occurred while processing the request.");
        }
    }
}
