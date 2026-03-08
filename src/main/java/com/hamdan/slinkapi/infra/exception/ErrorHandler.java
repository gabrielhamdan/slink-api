package com.hamdan.slinkapi.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FieldValidationErrDto>> handleErr400(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(e.getFieldErrors().stream().map(FieldValidationErrDto::new).toList());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrDto> handleErr401(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiErrDto(HttpStatus.UNAUTHORIZED.value(), "Não autorizado."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrDto> handleErr403(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiErrDto(HttpStatus.FORBIDDEN.value(), "Acesso negado."));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrDto> handleErr404(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrDto(HttpStatus.NOT_FOUND.value(), "Entidade não encontrada."));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrDto> handleErr404(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrDto(HttpStatus.NOT_FOUND.value(), "Recurso não encontrado."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrDto> handleErr500(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno do servidor."));
    }

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<ApiErrDto> handleGenericErr(ApiErrorException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new ApiErrDto(e));
    }

    public record FieldValidationErrDto(String field, String error) {
        public FieldValidationErrDto(FieldError fieldError) {
            this(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }

    public record ApiErrDto(int status, String message) {
        public ApiErrDto(ApiErrorException e) {
            this(e.getHttpStatus().value(), e.getMessage());
        }
    }

}
