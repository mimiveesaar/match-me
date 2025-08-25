package tech.kood.match_me.user_management.features.user.actions.getUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;

import java.util.UUID;

@ApplicationLayer
public record GetUserByEmailRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                    @NotNull @Valid @JsonProperty("email") EmailDTO email,
                                    @Nullable @JsonProperty("tracing_id") String tracingId) {

    public GetUserByEmailRequest(@NotNull EmailDTO email, @Nullable String tracingId) {
        this(UUID.randomUUID(), email, tracingId);
    }

    public GetUserByEmailRequest withRequestId(UUID requestId) {
        return new GetUserByEmailRequest(requestId, email, tracingId);
    }

    public GetUserByEmailRequest withEmail(EmailDTO email) {
        return new GetUserByEmailRequest(requestId, email, tracingId);
    }

    public GetUserByEmailRequest withTracingId(String tracingId) {
        return new GetUserByEmailRequest(requestId, email, tracingId);
    }
}