package io.selferral.admin.api.core.exception;

import io.selferral.admin.api.core.CD.ErrorCode;

public class TetherMaxException extends RuntimeException {
	private static final long serialVersionUID = -2899447744777394044L;
	
	private ErrorCode errorCode;

    public TetherMaxException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public TetherMaxException(String message) {
        super(message);
    }

    public TetherMaxException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}