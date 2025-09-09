package tech.kood.match_me.profile.service;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.dto.ProfileViewDTO;
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

        if (dto.getUsername() != null) {
            profile.setUsername(dto.getUsername());
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
            profile.setInterests(dto.getInterestIds().stream()
                    .map(id -> interestRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("Interest not found")))
                    .collect(Collectors.toSet()));
        }
        if (dto.getProfilePic() != null) {
            profile.setProfilePic(dto.getProfilePic());
        }

        Profile savedProfile = profileRepo.save(profile);

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
        System.out.println("=== Getting profile ===");
        long profileCount = profileRepo.count();
        System.out.println("Total profiles in database: " + profileCount);

        return profileRepo.findAllWithRelations().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No profile found"));
    }

    private ProfileViewDTO toViewDTO(Profile profile) {
        ProfileViewDTO dto = new ProfileViewDTO();
        dto.setId(profile.getId());
        dto.setUsername(profile.getUsername());
        dto.setAge(profile.getAge());
        dto.setHomeplanet(profile.getHomeplanet().getName());
        dto.setBodyform(profile.getBodyform().getName());
        dto.setLookingFor(profile.getLookingFor().getName());
        dto.setBio(profile.getBio());
        dto.setInterests(
                profile.getInterests().stream().map(i -> i.getName()).collect(Collectors.toSet()));
        dto.setProfilePic(profile.getProfilePic());
        return dto;
    }
}
