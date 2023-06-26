package com.apexon.compass.exception.handler;

import com.apexon.compass.exception.custom.*;
import com.apexon.compass.exception.dto.ApiError;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotWritable(HttpMessageNotWritableException ex) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<ApiError> handleServiceException(ServiceException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(UnauthorizedException ex) {
        ApiError apiError = new ApiError(UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbiddenException(ForbiddenException ex) {
        ApiError apiError = new ApiError(FORBIDDEN);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ApiError> handleJwtTokenException(JwtTokenException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleIOException(IOException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    protected ResponseEntity<ApiError> handleRecordNotFoundExceptionException(RecordNotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(TypeConversionException.class)
    protected ResponseEntity<ApiError> handleTypeConversionExceptionException(TypeConversionException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    protected ResponseEntity<ApiError> handleServiceUnavailableException(ServiceUnavailableException ex) {
        ApiError apiError = new ApiError(HttpStatus.SERVICE_UNAVAILABLE);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(EncryptionException.class)
    protected ResponseEntity<ApiError> handleEncryptionException(EncryptionException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
    /*
     * TODO check if NoHandlerFoundException is required to be handle. it is part of
     * webmvc
     *
     * @ExceptionHandler(NoHandlerFoundException.class) protected ResponseEntity<ApiError>
     * handleNoHandlerFoundException(NoHandlerFoundException ex) { ApiError apiError = new
     * ApiError(BAD_REQUEST); apiError.setMessage( String.format(
     * "Could not find the %s method for URL %s", ex.getHttpMethod(),
     * ex.getRequestURL())); apiError.setDebugMessage(ex.getRequestURL()); return
     * buildResponseEntity(apiError); }
     */
    /*
     * todo Followings are throwable exception cannot be catched here....
     *
     * @ExceptionHandler(HttpMediaTypeNotSupportedException.class) protected
     * ResponseEntity<ApiError> handleHttpMediaTypeNotSupported(
     * HttpMediaTypeNotSupportedException ex) { StringBuilder builder = new
     * StringBuilder(); builder.append(ex.getContentType());
     * builder.append(" media type is not supported. Supported media types are ");
     * ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", ")); return
     * buildResponseEntity( new ApiError( HttpStatus.UNSUPPORTED_MEDIA_TYPE,
     * builder.substring(0, builder.length() - 2), ex)); }
     *
     * @ExceptionHandler(MissingRequestHeaderException.class) protected
     * ResponseEntity<ApiError> handleMissingRequestHeader(MissingRequestHeaderException
     * ex) { return buildResponseEntity( new ApiError(BAD_REQUEST, ex.getHeaderName() +
     * " header is missing", ex)); }
     *
     * @ExceptionHandler(MissingServletRequestParameterException.class) protected
     * ResponseEntity<ApiError> handleMissingServletRequestParameter(
     * MissingServletRequestParameterException ex) { return buildResponseEntity( new
     * ApiError(BAD_REQUEST, ex.getParameterName() + " parameter is missing", ex)); }
     */

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
