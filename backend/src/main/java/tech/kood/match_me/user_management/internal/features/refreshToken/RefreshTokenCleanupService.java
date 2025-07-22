package tech.kood.match_me.user_management.internal.features.refreshToken;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import tech.kood.match_me.user_management.UserManagementConfig;
import tech.kood.match_me.user_management.internal.database.repostitories.RefreshTokenRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RefreshTokenCleanupService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserManagementConfig userManagementConfig;
    private final TaskScheduler taskScheduler;

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenCleanupService.class);

    private ScheduledFuture<?> scheduledTask;

    public RefreshTokenCleanupService(
            RefreshTokenRepository refreshTokenRepository, 
            @Qualifier("userManagementConfig") UserManagementConfig userManagementConfig,
            @Qualifier("userManagementTaskScheduler") TaskScheduler taskScheduler
        ) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.userManagementConfig = userManagementConfig;
        this.taskScheduler = taskScheduler;
    }

    @PostConstruct
    public void init() {
        // Schedule the cleanup task at application startup
        start();
    }

    public void start() {
        this.scheduledTask = taskScheduler.scheduleAtFixedRate(cleanupExpiredTokens(), Duration.ofSeconds(userManagementConfig.getRefreshTokenCleanupInterval() * 1000L));
    }

    public void stop() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }
    }

    private Runnable cleanupExpiredTokens() {
        return () -> {
            Instant now = Instant.now();
            refreshTokenRepository.deleteExpiredTokens(now);
            logger.info("Expired refresh tokens cleaned up at {}", now);
        };
    }
}