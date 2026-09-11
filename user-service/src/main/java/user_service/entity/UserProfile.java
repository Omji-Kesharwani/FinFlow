package user_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
public class UserProfile {

    @Id
    private UUID id;

    @Column(name = "first_name" , nullable = false , length = 100)
    private String firstName ;

    @Column(name = "last_name" , length = 100)
    private  String lastName ;

    @Column(length = 20)
    private String phone ;

    @Column(length = 500)
    private String address ;

    @Column(name = "created_at" , nullable = false)
    private OffsetDateTime createdAt ;

    @Column(name = "updated_at" , nullable = false)
    private OffsetDateTime updatedAt ;
}
