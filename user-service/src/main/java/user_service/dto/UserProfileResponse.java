package user_service.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String firstName ,
        String lastName ,
        String phone ,
        String address ,
        OffsetDateTime createdAt ,
        OffsetDateTime updatedAt
) {
}
