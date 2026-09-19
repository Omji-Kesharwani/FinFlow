package wallet_service.dto;

import java.time.OffsetDateTime;

public record ErrorResponse(
        int status ,
        String code ,
        String message ,
        OffsetDateTime timestamp
) {

}
