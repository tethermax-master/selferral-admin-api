package io.selferral.admin.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.selferral.admin.api.config.annotation.MasterAuthorize;
import io.selferral.admin.api.controller.path.APIPath;
import io.selferral.admin.api.model.request.UidEnrollListRequest;
import io.selferral.admin.api.model.request.UpdateUidEnrollRequest;
import io.selferral.admin.api.model.response.APIResponse;
import io.selferral.admin.api.model.response.DashBoardResponse;
import io.selferral.admin.api.model.response.UidEnrollListResponse;
import io.selferral.admin.api.service.DashBoardService;
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
	
	@GetMapping(path=APIPath.GET_DASHBOARD_INFO) 
	public APIResponse getDashBoard() throws Exception {
		DashBoardResponse res = dashBoardService.getDashBoardInfo();
		return new APIResponse(HttpStatus.OK.value(), res , "S010");
	}

	@GetMapping(path=APIPath.UID_ENROLL_LIST_EXCEL_DOWNLOAD) 
	public APIResponse uidEnrollListExcelDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		uidEnrollService.uidEnrollListExcelDownload(request, response);
		return new APIResponse(HttpStatus.OK.value(), null , "S011");
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
}