package zw.co.rbz.crb_backend.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    @JsonProperty("error")
    private final String error;

    @JsonProperty("error_description")
    private final String errorDescription;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("details")
    private final Map<String, String> details;

    public ErrorResponse(String error, String errorDescription, Map<String, String> details) {
        this.error = error;
        this.errorDescription = errorDescription;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(String error, String errorDescription) {
        return new ErrorResponse(error, errorDescription, null);
    }

    public static ErrorResponse withDetails(String error, String errorDescription, Map<String, String> details) {
        return new ErrorResponse(error, errorDescription, details);
    }
}
