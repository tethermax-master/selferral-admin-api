package io.selferral.admin.api.model.response;

import io.selferral.admin.api.core.CD.APIReponseCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class APIResponse {
	private int status;
	private Object result;
    private String code;
    private String message;
    
    public APIResponse(int httpCode, Object result, String code) {
    	this.status = httpCode;
    	this.result = result;
    	this.code = code;
        this.message = APIReponseCode.getCode(code);
    }
}