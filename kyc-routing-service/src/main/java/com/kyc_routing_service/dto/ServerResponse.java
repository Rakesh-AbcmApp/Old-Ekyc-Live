package com.kyc_routing_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper for all endpoints")
public class ServerResponse {

    @Schema(description = "HTTP response code", example = "200")
    private int responseCode;

    @Schema(description = "Response message", example = "Success")
    private String massage;

    @Schema(description = "Actual response data (can be object, list, or null)")
    private Object data;

    public ServerResponse(int responseCode, String message, Object data) {
        this.responseCode = responseCode;
        this.massage = message;
        this.data = data;
    }
}
