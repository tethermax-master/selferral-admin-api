package io.selferral.admin.api.controller.path;

public interface APIPath {
	/* Sign 관련 API start */
	// 로그인
	public static final String SIGN_IN = "/sign-in";
	// 패스워드 검증
	public static final String CHECK_PASSWORD = "/check-password";
	
	/* DashBoard 관련 API start */
	// 대시보드 정보 조회
	public static final String GET_DASHBOARD_INFO = "/get-dashboard-info";
	// UID 등록 리스트 엑셀 다운로드
	public static final String UID_ENROLL_LIST_EXCEL_DOWNLOAD = "/uid-enroll-list-excel-download";
	
	/* UID 신청 관련 API start */
	// UID 신청 리스트 조회
	public static final String GET_UID_ENROLL_LIST = "/get-uid-enroll-list";
	// UID 신청 업데이트
	public static final String UPDATE_UID_ENROLL = "/update-uid-enroll";

	/* 셀퍼럴 후기 관련 API start */
	// 거래소 리스트 조회
	public static final String GET_EXCHANGE_LIST = "/get-exchange-list";
	// 셀퍼럴 후기 리스트 조회
	public static final String GET_SELFERRAL_REVIEW_LIST = "/get-selferral-review-list";
	// 셀퍼럴 후기 삭제
	public static final String DELETE_SELFERRAL_REVIEW = "/delete-selferral-review";
}