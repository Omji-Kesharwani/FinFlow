package user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserProfileRequest(
  @NotBlank
  @Size(max = 100)
  String firstName ,

  @Size(max = 100)
  String lastName ,

  @Size(max = 20)
  String phone ,

  @Size(max = 500)
  String address
){
}
