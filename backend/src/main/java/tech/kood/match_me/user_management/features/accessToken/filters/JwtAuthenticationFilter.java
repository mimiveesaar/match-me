package tech.kood.match_me.user_management.features.accessToken.filters;

import java.io.IOException;

import org.jmolecules.architecture.layered.ApplicationLayer;
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
import tech.kood.match_me.user_management.features.accessToken.actions.ValidateAccessToken;
import tech.kood.match_me.user_management.features.user.actions.GetUserById;

@ApplicationLayer
@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final ValidateAccessToken.Handler validateAccessTokenHandler;
    private final GetUserById.Handler getUserByIdQueryHandler;

    public JwtAuthenticationFilter(ValidateAccessToken.Handler validateAccessTokenHandler, GetUserById.Handler getUserByIdQueryHandler) {
        this.validateAccessTokenHandler = validateAccessTokenHandler;
        this.getUserByIdQueryHandler = getUserByIdQueryHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
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

        try {
            var validationRequest = new ValidateAccessToken.Request(jwt);
            var validationResult = validateAccessTokenHandler.handle(validationRequest);

            if (validationResult instanceof ValidateAccessToken.Result.Success successResult) {
                var userId = successResult.userId();

                var getUserByIdRequest = new GetUserById.Request(userId);
                var userResult = getUserByIdQueryHandler.handle(getUserByIdRequest);

                if (userResult instanceof GetUserById.Result.Success user) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user.user(), null, null);

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else if (userResult instanceof GetUserById.Result.UserNotFound) {
                    logger.warn("User not found for ID: {} during JWT authentication", userId.toString());

                } else if (userResult instanceof GetUserById.Result.SystemError systemError) {
                    logger.error("System error while fetching userId by ID: {} during JWT authentication. Error: {}", userId.toString(), systemError.message());
                }
            } else if (validationResult instanceof ValidateAccessToken.Result.InvalidToken) {
                logger.debug("Invalid JWT token provided for authentication");
            }
        } catch (Exception e) {
            logger.error("Unexpected error during JWT authentication", e);
        }

        filterChain.doFilter(request, response);
    }
}