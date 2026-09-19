package wallet_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PageRequestDto (
        @Min(value = 0, message = "Page cannot be negative")
        Integer page,

        @Min(value = 1, message = "Size must be at least 1")
        @Max(value = 100, message = "Size cannot exceed 100")
        Integer size
){
    public int getPage(){
        return page == null ? 0 : page ;
    }

    public int getSize(){
        return size == null ? 20 : size ;
    }
}
