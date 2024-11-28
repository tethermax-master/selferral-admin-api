package io.selferral.admin.api.core;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CD {
	

    public enum useYn{
    	Y, N 
    }
    
    public enum EnrollStatus {
    	S, R, P // (승인, 거절, 진행중)
    }
    
    public enum ExchangeName {
    	BINGX, BITGET, BITMART, BYBIT, COINCATCH, DEEPCOIN, MEXC, OKX;
    }

    public enum DeleteYn{
    	Y, N 
    }
    
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum APIReponseCode {
		// SIGN
		SERVER_TIME_STAMP(200, "S001", "서버시간 조회 성공."),
//		SIGN_IN_SUCCESS(200, "S002", "로그인 성공"),
		EXCHANGE_LIST_SUCCESS(200, "S030", "거래소 목록 조회 성공."),
		DELETE_EXCHANGE_REVIEW(200, "S031", "거래소 후기 삭제 성공."),
		EXCHANGE_REVIEW_LIST(200, "S032", "거래소 후기 리스트 조회 성공."),
		
		
		;
		
		private int status;
	    private final String code;
	    private final String message;
	    
	    APIReponseCode(final int status, final String code, final String message) {
	        this.status = status;
	        this.code = code;
	        this.message = message;
	    }
	    
	    public int getStatus() {
	    	return status;
	    }

	    public String getCode() {
	    	return code;
	    }

	    public String getMessage() {
	        return this.message;
	    }
	    
	    public static String getCode(String code) {
	        for (APIReponseCode responseCode : APIReponseCode.values()) {
	            if (code.equals(responseCode.code)) {
	                return responseCode.getMessage();
	            }
	        }
	        return null;
	    }
	}
    
    
}	