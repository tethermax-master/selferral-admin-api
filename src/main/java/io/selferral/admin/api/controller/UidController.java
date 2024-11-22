package io.selferral.admin.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.selferral.admin.api.core.APIPath;
import io.selferral.admin.api.core.CD.UidEnrollProcessResponse;
import io.selferral.admin.api.model.request.UidEnrollInitRequest;
import io.selferral.admin.api.model.response.APIResponse;
import io.selferral.admin.api.service.UidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/uid")
@RequiredArgsConstructor
public class UidController {
	
	private final UidService uidService;
	
	@PostMapping(path=APIPath.UID_ENROLL_INIT)
	public APIResponse initUidEnroll(@RequestBody @Valid UidEnrollInitRequest req) throws Exception {
		UidEnrollProcessResponse res = uidService.enrollUidProcess(req.exchangeNo(),req.uid());
		return new APIResponse(HttpStatus.OK.value(), res , "S100");
	}

}
