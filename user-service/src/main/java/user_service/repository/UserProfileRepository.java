package user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import user_service.entity.UserProfile;

import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
}
