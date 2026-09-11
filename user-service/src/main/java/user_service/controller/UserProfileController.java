package user_service.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import user_service.dto.CreateUserProfileRequest;
import user_service.dto.UpdateUserProfileRequest;
import user_service.dto.UserProfileResponse;
import user_service.service.UserProfileService;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {
    private final UserProfileService userProfileService ;

    public UserProfileController(UserProfileService userProfileService){
        this.userProfileService = userProfileService ;
    }

    @PostMapping("/profile")
    public ResponseEntity<UserProfileResponse> createProfile(Authentication authentication, @Valid @RequestBody CreateUserProfileRequest request){
       UUID userId = (UUID) authentication.getPrincipal() ;

        UserProfileResponse response = userProfileService.createProfile(userId,request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication){

        UUID userId = (UUID) authentication.getPrincipal() ;
        UserProfileResponse response = userProfileService.getProfile(userId);

        return  ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse>updateProfile(
            Authentication authentication ,
            @Valid @RequestBody UpdateUserProfileRequest request
            )
    {
        UUID userId = (UUID) authentication.getPrincipal() ;
        UserProfileResponse response = userProfileService.updateProfile(userId,request);
        return ResponseEntity.ok(response) ;
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile(Authentication authentication){
        UUID userId = (UUID) authentication.getPrincipal() ;
        userProfileService.deleteProfile(userId);
        return ResponseEntity.noContent().build() ;
    }
}
