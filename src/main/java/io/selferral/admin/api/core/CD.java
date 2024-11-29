package io.selferral.admin.api.core;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.selferral.admin.api.core.util.EnumMapperType;

public class CD {
	

	public enum UidEnrollConfirmStatus {
		SUCCESS, REJECT;
	}

	public enum UidEnrollStatus {
		S, P, R;	// 완료, 대기중, 거절
	}
    
    public enum MemberRole implements EnumMapperType {
		MASTER("어드민"),
		;

		private String label;
		
		MemberRole(String label) { this.label = label; }
		
		@Override
		public String getCode() { return name(); }

		@Override
		public String getLabel() { return label; }
		
	    public static String getMemberRole(String code) {
	        for (MemberRole gubun : MemberRole.values()) {
	            if (code.equals(gubun.getCode())) {
	                return gubun.getLabel();
	            }
	        }
	        return null;
	    }
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
		SIGN_IN_SUCCESS(200, "S001", "로그인 성공"),

		// DASHBOARD
		GET_DASHBOARD_INFO_SUCCESS(200, "S010", "대시보드 조회 성공"),
		UID_ENROLL_LIST_EXCEL_DOWNLOAD_SUCCESS(200, "S011", "UID 리스트 엑셀 다운로드 성공"),
		
		// UID ENROLL
		GET_UID_ENROLL_LIST_SUCCESS(200, "S012", "UID 신청 내역 조회 성공"),
		UPDATE_UID_ENROLL_SUCCESS(200, "S013", "UID 신청 상태 업데이트 성공"),
		
		// SELFERRAL REVIEW
		GET_EXCHANGE_LIST_SUCCESS(200, "S030", "거래소 목록 조회 성공."),
		GET_SELFERRAL_REVIEW_LIST_SUCCESS(200, "S031", "거래소 후기 리스트 조회 성공."),
		DELETE_SELFERRAL_REVIEW_SUCCESS(200, "S032", "거래소 후기 삭제 성공."),
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
    
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum ErrorCode {

	    // Common
		BAD_REQUEST(400, "E000", "잘못된 요청 입니다."),
	    INVALID_INPUT_VALUE(400, "E001", "입력 값이 잘못 되었습니다."),
	    METHOD_NOT_ALLOWED(405, "E002", "허용되지 않은 요청 방식 입니다."),
	    INTERNAL_SERVER_ERROR(500, "E003", "서버에서 발생한 오류입니다. 운영자에게 신고부탁드립니다."),
	    INVALID_TYPE_VALUE(400, "E004", "잘못된 형식의 갑을 입력하였습니다."),
	    REQUIRED_PARAMETER_IS_MISSING(400, "E005", "필수 parameter가 없습니다.다시 확인해주세요."),
	    
	    // Sign
	    INVALID_JWT_SIGNATURE(401, "E006", "잘못된 JWT 서명입니다."),
	    INVALID_JWT_TOKEN(401, "E007", "유효하지 않은 토큰입니다."),
	    EXPIRED_JWT_TOKEN(401, "E008", "토큰 기한 만료"),
	    NOT_SUPPORTED_JWT_TOKEN(401, "E009", "지원하지 않는 토큰입니다."),
	    UNAUTHORIZED(401, "E010", "권한이 없습니다. 로그인이 필요합니다."),
	    FORBIDDEN(403, "E011", "접근 권한이 없습니다."),
	    
	    // Member
	    INVALID_ID_OR_PASSWORD(400, "E021", "아이디 또는 비밀번호가 일치하지 않습니다."),
	    NOT_REGISTERED_MEMBER(400, "E022", "가입된 회원이 아닙니다. 회원 가입을 먼저 진행해주세요."),
	    
	    // UidEnroll
	    NOT_PROCCESSING_STATUS(400, "E030", "신청 중인 상태가 아닙니다."),
	    ;
		
		private int status;
		private final String code;
	    private final String message;

	    ErrorCode(final int status, final String code, final String message) {
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
	        for (ErrorCode errorCode : ErrorCode.values()) {
	            if (code.equals(errorCode.code)) {
	                return errorCode.getMessage();
	            }
	        }
	        return null;
	    }
	}
    
}	