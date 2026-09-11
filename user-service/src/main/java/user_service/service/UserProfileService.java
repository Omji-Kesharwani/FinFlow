package user_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_service.dto.CreateUserProfileRequest;
import user_service.dto.UpdateUserProfileRequest;
import user_service.dto.UserProfileResponse;
import user_service.entity.UserProfile;
import user_service.exception.UserProfileAlreadyExistsException;
import user_service.exception.UserProfileNotFoundException;
import user_service.repository.UserProfileRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository ;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public UserProfileResponse createProfile(UUID userId , CreateUserProfileRequest request){

        if(userProfileRepository.existsById(userId))
        {
            throw new UserProfileAlreadyExistsException("User profile Already exists");
        }

        OffsetDateTime now = OffsetDateTime.now() ;

        UserProfile profile = new UserProfile();

        profile.setId(userId);
        profile.setFirstName(request.firstName());
        profile.setLastName(request.lastName());
        profile.setPhone(request.phone());
        profile.setAddress(request.address());
        profile.setCreatedAt(now);
        profile.setUpdatedAt(now);

        UserProfile savedProfile = userProfileRepository.save(profile);
        return toResponse(savedProfile);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(UUID userId){
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(()->new UserProfileNotFoundException("User profile not found "));

        return toResponse(profile);
    }

    public UserProfileResponse updateProfile(UUID userId , UpdateUserProfileRequest request){
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(()->new UserProfileNotFoundException(
                        "User profile not found"
                ));
        profile.setFirstName(request.firstName());
        profile.setLastName(request.lastName());
        profile.setPhone(request.phone());
        profile.setAddress(request.address());
        profile.setUpdatedAt(OffsetDateTime.now());

        UserProfile updatedProfile = userProfileRepository.save(profile);
        return toResponse(updatedProfile);
    }

    @Transactional
    public void deleteProfile(UUID userId){
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(()->new UserProfileNotFoundException("User profile not found"));
        userProfileRepository.delete(profile);
    }



    private UserProfileResponse toResponse(UserProfile profile){
        return new UserProfileResponse(
                profile.getId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhone(),
                profile.getAddress(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
