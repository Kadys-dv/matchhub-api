package dev.kadys.matchhub.error;

import org.springframework.http.HttpStatus;
import java.time.Instant;
import java.util.Map;

public record ApiError(Instant timestamp,int status,String error,String message,String path,Map<String,String> fields) {
    public static ApiError of(HttpStatus status,String message,String path,Map<String,String> fields) {
        return new ApiError(Instant.now(),status.value(),status.getReasonPhrase(),message,path,fields);
    }
}
