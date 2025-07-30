package tech.kood.match_me.user_management.api.filters;

import java.io.IOException;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.kood.match_me.user_management.internal.features.getUser.GetUserHandler;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByIdRequest;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenHandler;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenResults;
import tech.kood.match_me.user_management.models.AccessToken;


@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final ValidateAccessTokenHandler validateAccessTokenHandler;

    private final GetUserHandler getUserHandler;

    public JwtAuthenticationFilter(ValidateAccessTokenHandler validateAccessTokenHandler,
            GetUserHandler getUserHandler) {
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
        if (jwt != null) {
            var validationRequest = new ValidateAccessTokenRequest(UUID.randomUUID().toString(),
                    jwt, UUID.randomUUID().toString());

            var validationResult = validateAccessTokenHandler.handle(validationRequest);

            switch (validationResult) {
                case ValidateAccessTokenResults.Success(AccessToken accessToken, String tracingId) -> {
                    var userId = accessToken.userId();

                    var getUserByIdRequest =
                            new GetUserByIdRequest(UUID.randomUUID().toString(), userId, tracingId);
                    var userDetails = getUserHandler.handle(getUserByIdRequest);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, null);

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                case ValidateAccessTokenResults.InvalidToken(String accessToken, String tracingId) -> {
                }
                case ValidateAccessTokenResults.InvalidRequest(String message, String tracingId) -> {
                }
            }

        }

        filterChain.doFilter(request, response);
    }
}
