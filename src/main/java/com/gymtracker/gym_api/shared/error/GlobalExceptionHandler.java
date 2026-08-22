package com.gymtracker.gym_api.shared.error;


import com.gymtracker.gym_api.shared.exception.BusinessException;
import com.gymtracker.gym_api.shared.exception.NotFoundException;
import com.gymtracker.gym_api.shared.exception.auth.InvalidCredentialsException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "La solicitud contiene datos inválidos", request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowed(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP no permitido", request);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ApiError> handleUnauthorized(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "Se requiere autenticación", request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleIntegrity(Exception ex, HttpServletRequest request) {
        log.warn("Conflicto de integridad en {}", request.getRequestURI(), ex);
        return buildError(HttpStatus.CONFLICT,
                "La operación entra en conflicto con datos existentes", request);
    }

    // 422
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos");

        return buildError(HttpStatusCode.valueOf(422), message, request);
    }

    // 409
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(
            BusinessException ex,
            HttpServletRequest request) {

        return buildError(HttpStatusCode.valueOf(409), ex.getMessage(), request);
    }

    // 404
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            NotFoundException ex,
            HttpServletRequest request) {

        return buildError(HttpStatusCode.valueOf(404), ex.getMessage(), request);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        log.error("Error inesperado en {}", request.getRequestURI(), ex);
        return buildError(
                HttpStatusCode.valueOf(500),
                "Error interno del servidor",
                request
        );
    }

    private ResponseEntity<ApiError> buildError(
            HttpStatusCode status,
            String message,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                Instant.now(),
                status.value(),
                HttpStatus.valueOf(status.value()).getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status.value()).body(error);
    }
}
