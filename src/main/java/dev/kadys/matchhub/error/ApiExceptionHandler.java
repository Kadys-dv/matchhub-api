package dev.kadys.matchhub.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(NotFoundException.class) ResponseEntity<ApiError> notFound(NotFoundException e,HttpServletRequest r){return error(HttpStatus.NOT_FOUND,e.getMessage(),r,Map.of());}
    @ExceptionHandler(ConflictException.class) ResponseEntity<ApiError> conflict(ConflictException e,HttpServletRequest r){return error(HttpStatus.CONFLICT,e.getMessage(),r,Map.of());}
    @ExceptionHandler(UnauthorizedException.class) ResponseEntity<ApiError> unauthorized(UnauthorizedException e,HttpServletRequest r){return error(HttpStatus.UNAUTHORIZED,e.getMessage(),r,Map.of());}
    @ExceptionHandler(ForbiddenException.class) ResponseEntity<ApiError> forbidden(ForbiddenException e,HttpServletRequest r){return error(HttpStatus.FORBIDDEN,e.getMessage(),r,Map.of());}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException e,HttpServletRequest r){
        Map<String,String> fields=new LinkedHashMap<>(); e.getBindingResult().getFieldErrors().forEach(x -> fields.putIfAbsent(x.getField(),x.getDefaultMessage()));
        return error(HttpStatus.BAD_REQUEST,"Dados inválidos.",r,fields);
    }
    private ResponseEntity<ApiError> error(HttpStatus status,String message,HttpServletRequest request,Map<String,String> fields){
        return ResponseEntity.status(status).body(ApiError.of(status,message,request.getRequestURI(),fields));
    }
}
