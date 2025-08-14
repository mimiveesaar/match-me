package tech.kood.match_me.user_management.api.filters;

import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.kood.match_me.user_management.internal.domain.models.AccessToken;
import tech.kood.match_me.user_management.internal.features.getUser.handlers.GetUserByIdHandler;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByIdQuery;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdQueryResults;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenHandler;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenResults;


@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final ValidateAccessTokenHandler validateAccessTokenHandler;

    private final GetUserByIdHandler getUserHandler;

    public JwtAuthenticationFilter(ValidateAccessTokenHandler validateAccessTokenHandler,
            GetUserByIdHandler getUserHandler) {
        this.validateAccessTokenHandler = validateAccessTokenHandler;
        this.getUserHandler = getUserHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.substring(7);
        if (jwt == null || jwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Generate consistent request IDs for tracing
        UUID validationRequestId = UUID.randomUUID();
        String tracingId = UUID.randomUUID().toString();
        
        try {
            var validationRequest = ValidateAccessTokenRequest.of(validationRequestId, jwt, tracingId);
            var validationResult = validateAccessTokenHandler.handle(validationRequest);

            if (validationResult instanceof ValidateAccessTokenResults.Success successResult) {
                AccessToken accessToken = successResult.getAccessToken();
                var userId = accessToken.getUserId();

                UUID getUserRequestId = UUID.randomUUID();
                var getUserByIdRequest = GetUserByIdQuery.of(getUserRequestId, userId, tracingId);
                var userResult = getUserHandler.handle(getUserByIdRequest);

                if (userResult instanceof GetUserByIdQueryResults.Success userSuccess) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userSuccess.getUser(), null, null);

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else if (userResult instanceof GetUserByIdQueryResults.UserNotFound) {
                    logger.warn("User not found for ID: {} during JWT authentication", userId.getValue());
                    // Authentication will not be set, allowing the request to proceed unauthenticated
                    // This will likely be handled by Spring Security configuration
                } else if (userResult instanceof GetUserByIdQueryResults.SystemError systemError) {
                    logger.error("System error while fetching user by ID: {} during JWT authentication. Error: {}", 
                            userId.getValue(), systemError.getMessage());
                    // Authentication will not be set due to system error
                }
            } else if (validationResult instanceof ValidateAccessTokenResults.InvalidToken) {
                logger.debug("Invalid JWT token provided for authentication");
                // Token is invalid, authentication will not be set
                // This will likely be handled by Spring Security configuration
            }
        } catch (Exception e) {
            logger.error("Unexpected error during JWT authentication", e);
            // In case of unexpected errors, we don't set authentication
            // This will likely be handled by Spring Security configuration
        }

        filterChain.doFilter(request, response);
    }
}
