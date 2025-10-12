package tech.kood.match_me.connections.features.pendingConnection.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.GetOutgoingConnectionRequests;
import tech.kood.match_me.connections.features.pendingConnection.domain.PendingConnectionDTO;
import tech.kood.match_me.connections.features.pendingConnection.internal.mapper.PendingConnectionMapper;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;

import java.util.ArrayList;

@ApplicationLayer
@Service
public class GetOutgoingConnectionQueryHandlerImpl implements GetOutgoingConnectionRequests.Handler {

    private final Validator validator;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final PendingConnectionMapper pendingConnectionMapper;

    public GetOutgoingConnectionQueryHandlerImpl(Validator validator, PendingConnectionRepository pendingConnectionRepository, PendingConnectionMapper pendingConnectionMapper) {
        this.validator = validator;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.pendingConnectionMapper = pendingConnectionMapper;
    }

    @Override
    public GetOutgoingConnectionRequests.Result handle(GetOutgoingConnectionRequests.Request request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new GetOutgoingConnectionRequests.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            var outgoingRequests = pendingConnectionRepository.findBySender(request.userId().value());
            var outgoingRequestsMapped = new ArrayList<PendingConnectionDTO>();
            for (PendingConnectionEntity outgoingRequest : outgoingRequests) {
                PendingConnectionDTO dto = pendingConnectionMapper.toDTO(outgoingRequest);
                outgoingRequestsMapped.add(dto);
            }

            return new GetOutgoingConnectionRequests.Result.Success(outgoingRequestsMapped);
        } catch (Exception e) {
            return new GetOutgoingConnectionRequests.Result.SystemError("An unexpected error occurred while processing the request.");
        }
    }
}
