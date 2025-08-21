package tech.kood.match_me.profile.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.model.Bodyform;
import tech.kood.match_me.profile.model.Interest;
import tech.kood.match_me.profile.model.LookingFor;
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

    public ProfileService(ProfileRepository profileRepo,
                          HomeplanetRepository homeplanetRepo,
                          BodyformRepository bodyformRepo,
                          LookingForRepository lookingForRepo,
                          InterestRepository interestRepo) {
        this.profileRepo = profileRepo;
        this.homeplanetRepo = homeplanetRepo;
        this.bodyformRepo = bodyformRepo;
        this.lookingForRepo = lookingForRepo;
        this.interestRepo = interestRepo;
    }

    @Transactional
    public Profile createProfile(ProfileDTO dto) {
        Profile profile = new Profile();
        profile.setId(UUID.randomUUID());
        profile.setUsername(dto.getUsername());
        profile.setAge(dto.getAge());
        profile.setBio(dto.getBio());
        profile.setProfilePic(dto.getProfilePic());

        profile.setHomeplanet(homeplanetRepo.findById(dto.getHomeplanetId()).orElseThrow());
        profile.setBodyform((Bodyform) bodyformRepo.findById(dto.getBodyformId()).orElseThrow());
        profile.setLookingFor((LookingFor) lookingForRepo.findById(dto.getLookingForId()).orElseThrow());
        profile.setInterests(new java.util.HashSet<>(
            interestRepo.findAllById(dto.getInterestIds())
                .stream()
                .map(obj -> (Interest) obj)
                .toList()
        ));

        return profileRepo.save(profile);
    }

    public Profile getProfile(UUID id) {
        return profileRepo.findById(id).orElseThrow();
    }
}
