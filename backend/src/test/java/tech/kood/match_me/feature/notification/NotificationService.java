
package tech.kood.match_me.feature.notification;

import tech.kood.match_me.feature.order.ProfileCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @EventListener
    public void handle(ProfileCreatedEvent event) {
        System.out.println("Notifying about profileId: " + event.profileId());
        System.out.println("Notifying about username: " + event.name());
    }
}