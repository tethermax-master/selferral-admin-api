package io.selferral.admin.api.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import io.selferral.admin.api.core.CD.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Exception handleAllExceptions(RuntimeException e) {
    	log.error("Internal server error!!!.", e);
        return e;
    }
    
    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getFieldError().getDefaultMessage(), ErrorCode.REQUIRED_PARAMETER_IS_MISSING.getCode());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        final ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getBindingResult(), ErrorCode.INVALID_INPUT_VALUE.getCode());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * API 호출 시 필수 parameter가 없을 때 발생한다.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> missingServletRequestParameterException(MissingServletRequestParameterException e) {
    	log.error("missingServletRequestParameterException", e);
    	final ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), ErrorCode.INVALID_INPUT_VALUE.getCode());
    	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        final ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ErrorCode.INVALID_TYPE_VALUE.getCode());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), ErrorCode.METHOD_NOT_ALLOWED.getCode());
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }
    
    @ExceptionHandler(TetherMaxException.class)
    protected ResponseEntity<ErrorResponse> tetherMaxException(final TetherMaxException e) {
        log.error("tetherMaxException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = new ErrorResponse(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }
    
    /**
     * Spring Security에서 로그인한 유저의 권한이 없을 때 발생(User, Admin, Partner)
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> accessDeniedException(AccessDeniedException e) {
    	log.error("accessDeniedException", e);
    	final ErrorResponse response = new ErrorResponse(ErrorCode.FORBIDDEN);
    	return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.FORBIDDEN.getStatus()));
    }
    
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> authenticationException(Exception e) {
    	log.error("authenticationException", e);
        final ErrorResponse response = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ErrorCode.UNAUTHORIZED.getCode());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * Enum에 명시 안된 값을 시도 하였을때
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> httpMessageNotReadableException(Exception e) {
    	log.error("httpMessageNotReadableException", e);
    	final ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ErrorCode.BAD_REQUEST.getCode());
    	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}