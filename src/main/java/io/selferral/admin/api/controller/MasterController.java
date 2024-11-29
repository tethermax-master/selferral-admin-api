package io.selferral.admin.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.selferral.admin.api.config.annotation.MasterAuthorize;
import io.selferral.admin.api.controller.path.APIPath;
import io.selferral.admin.api.model.request.DeleteSelferralReviewRequest;
import io.selferral.admin.api.model.request.SelferralReivewRequest;
import io.selferral.admin.api.model.request.UidEnrollListRequest;
import io.selferral.admin.api.model.request.UpdateUidEnrollRequest;
import io.selferral.admin.api.model.response.APIResponse;
import io.selferral.admin.api.model.response.DashBoardResponse;
import io.selferral.admin.api.model.response.ExchangeResponse;
import io.selferral.admin.api.model.response.SelferralReviewListResponse;
import io.selferral.admin.api.model.response.UidEnrollListResponse;
import io.selferral.admin.api.service.DashBoardService;
import io.selferral.admin.api.service.SelferralReviewService;
import io.selferral.admin.api.service.UidEnrollService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@MasterAuthorize
@RestController
@RequestMapping("/admin")
public class MasterController {

	@Autowired
	DashBoardService dashBoardService;

	@Autowired
	UidEnrollService uidEnrollService;
	
	@Autowired
	SelferralReviewService selferralReviewService;
	
	
	@GetMapping(path=APIPath.GET_DASHBOARD_INFO) 
	public APIResponse getDashBoard() throws Exception {
		DashBoardResponse res = dashBoardService.getDashBoardInfo();
		return new APIResponse(HttpStatus.OK.value(), res , "S010");
	}

	@GetMapping(path=APIPath.UID_ENROLL_LIST_EXCEL_DOWNLOAD) 
	public void uidEnrollListExcelDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		uidEnrollService.uidEnrollListExcelDownload(request, response);
	}

	@GetMapping(path=APIPath.GET_UID_ENROLL_LIST) 
	public APIResponse getUidEnrollList(@Valid UidEnrollListRequest req) throws Exception {
		UidEnrollListResponse res = uidEnrollService.getUidEnrollList(req);
		return new APIResponse(HttpStatus.OK.value(), res , "S012");
	}
	
	@PutMapping(path=APIPath.UPDATE_UID_ENROLL) 
	public APIResponse updateUidEnroll(@RequestBody @Valid UpdateUidEnrollRequest req) throws Exception {
		boolean result = uidEnrollService.updateUidEnroll(req);
		return new APIResponse(HttpStatus.OK.value(), result , "S013");
	}
	
	@GetMapping(path=APIPath.GET_EXCHANGE_LIST)
	@Description("거래소 목록 조회")
	public APIResponse getExchangeList() throws Exception {
		List<ExchangeResponse> res = selferralReviewService.getExchangeList();
		return new APIResponse(HttpStatus.OK.value(),res, "S030") ;
	}
	
	@GetMapping(path=APIPath.GET_SELFERRAL_REVIEW_LIST)
	@Description("거래소 후기 리스트 조회")
	public APIResponse getSelferralReviewList(@Valid SelferralReivewRequest req) throws Exception {
		SelferralReviewListResponse res = selferralReviewService.getSelferralReviewList(req);
		 return new APIResponse(HttpStatus.OK.value(), res, "S031") ;
	}
	
	@PutMapping(path=APIPath.DELETE_SELFERRAL_REVIEW)
	@Description("거래소 후기 삭제")
	public APIResponse deleteSelferralReview (@RequestBody @Valid DeleteSelferralReviewRequest req) throws Exception {
		boolean result = selferralReviewService.deleteSelferralReview(req);
		return new APIResponse(HttpStatus.OK.value(), result, "S032");
	}
}