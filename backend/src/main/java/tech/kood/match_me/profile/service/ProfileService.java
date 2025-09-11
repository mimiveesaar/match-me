package tech.kood.match_me.profile.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.dto.ProfileViewDTO;
import tech.kood.match_me.profile.model.Interest;
import tech.kood.match_me.profile.model.Profile;
import tech.kood.match_me.profile.repository.BodyformRepository;
import tech.kood.match_me.profile.repository.HomeplanetRepository;
import tech.kood.match_me.profile.repository.InterestRepository;
import tech.kood.match_me.profile.repository.LookingForRepository;
import tech.kood.match_me.profile.repository.ProfileRepository;

@Service
public class ProfileService {

    private final ProfileRepository profileRepo;
    private final HomeplanetRepository homeplanetRepo;
    private final BodyformRepository bodyformRepo;
    private final LookingForRepository lookingForRepo;
    private final InterestRepository interestRepo;

    public ProfileService(ProfileRepository profileRepo, HomeplanetRepository homeplanetRepo,
            BodyformRepository bodyformRepo, LookingForRepository lookingForRepo,
            InterestRepository interestRepo) {
        this.profileRepo = profileRepo;
        this.homeplanetRepo = homeplanetRepo;
        this.bodyformRepo = bodyformRepo;
        this.lookingForRepo = lookingForRepo;
        this.interestRepo = interestRepo;
    }

    @Transactional("profileManagementTransactionManager")
    public ProfileViewDTO saveOrUpdateProfile(ProfileDTO dto) {
        Profile profile = getMyProfile(); // fetch existing profile

        if (dto.getName() != null) {
        System.out.println("Updating name to: " + dto.getName());
        profile.setUsername(dto.getName()); // Still save to username field in database
    }
        if (dto.getAge() != null) {
            profile.setAge(dto.getAge());
        }
        if (dto.getHomeplanetId() != null) {
            profile.setHomeplanet(homeplanetRepo.findById(dto.getHomeplanetId())
                    .orElseThrow(() -> new RuntimeException("Homeplanet not found")));
        }
        if (dto.getBodyformId() != null) {
            profile.setBodyform(bodyformRepo.findById(dto.getBodyformId())
                    .orElseThrow(() -> new RuntimeException("Bodyform not found")));
        }
        if (dto.getLookingForId() != null) {
            profile.setLookingFor(lookingForRepo.findById(dto.getLookingForId())
                    .orElseThrow(() -> new RuntimeException("LookingFor not found")));
        }
        if (dto.getBio() != null) {
            profile.setBio(dto.getBio());
        }
        if (dto.getInterestIds() != null && !dto.getInterestIds().isEmpty()) {
            // Clear existing interests first
            profile.getInterests().clear();

            // Add new interests
            profile.setInterests(dto.getInterestIds().stream()
                    .map(id -> interestRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("Interest not found")))
                    .collect(Collectors.toSet()));
        }
        if (dto.getProfilePic() != null) {
            profile.setProfilePic(dto.getProfilePic());
        }

        // Force a flush to ensure changes are persisted
        Profile savedProfile = profileRepo.saveAndFlush(profile);

        // Convert to DTO while still in transaction
        return toViewDTO(savedProfile);
    }

    @Transactional(value = "profileManagementTransactionManager", readOnly = true)
    public ProfileViewDTO getMyProfileDTO() {
        Profile profile = getMyProfile();
        return toViewDTO(profile);
    }

    @Transactional(value = "profileManagementTransactionManager", readOnly = true)
    public Profile getMyProfile() {

        return profileRepo.findAllWithRelations().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No profile found"));
    }

    private ProfileViewDTO toViewDTO(Profile profile) {
        ProfileViewDTO dto = new ProfileViewDTO();
        dto.setId(profile.getId());
        dto.setUsername(profile.getUsername());
        dto.setName(profile.getUsername());
        dto.setAge(profile.getAge());

        // Include both names and IDs
        dto.setHomeplanet(profile.getHomeplanet().getName());
        dto.setHomeplanetId(profile.getHomeplanet().getId().intValue()); // Add this

        dto.setBodyform(profile.getBodyform().getName());
        dto.setBodyformId(profile.getBodyform().getId().intValue()); // Add this

        dto.setLookingFor(profile.getLookingFor().getName());
        dto.setLookingForId(profile.getLookingFor().getId().intValue()); // Add this

        dto.setBio(profile.getBio());

        // Sort interests alphabetically by name
        List<Interest> sortedInterests = profile.getInterests().stream()
                .sorted((i1, i2) -> i1.getName().compareTo(i2.getName()))
                .collect(Collectors.toList());

        dto.setInterests(
                sortedInterests.stream().map(i -> i.getName()).collect(Collectors.toSet()));

        dto.setInterestIds(sortedInterests.stream().map(i -> i.getId().intValue())
                .collect(Collectors.toList()));

        dto.setProfilePic(profile.getProfilePic());

        return dto;
    }
}
