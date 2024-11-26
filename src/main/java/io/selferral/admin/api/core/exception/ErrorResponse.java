package io.selferral.admin.api.core.exception;

import io.selferral.admin.api.core.CD.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private int status;
    private Object result;
    private String code;
    private String message;

    public ErrorResponse(int httpCode, Object result, String code) {
    	this.status = httpCode;
    	this.result = result;
    	this.code = code;
        this.message = ErrorCode.getCode(code);
    }
    
    public ErrorResponse(int httpCode, String code) {
    	this.status = httpCode;
    	this.code = code;
    	this.message = ErrorCode.getCode(code);
    	
    }
    
	public ErrorResponse(final ErrorCode code) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
    }
}