
package tech.kood.match_me.feature.order;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ApplicationEventPublisher publisher;

    public ProfileService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void createProfile(String profileId, String name) {
        // logic to create profile...
        System.out.println("Profile created: " + profileId);
        publisher.publishEvent(new ProfileCreatedEvent(profileId, name));
    }
}
