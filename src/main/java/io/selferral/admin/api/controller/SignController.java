package io.selferral.admin.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.selferral.admin.api.controller.path.APIPath;
import io.selferral.admin.api.model.request.SignInRequest;
import io.selferral.admin.api.model.response.APIResponse;
import io.selferral.admin.api.service.SignService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/sign")
public class SignController {
	
	@Autowired
	SignService signService;
	
	@PostMapping(path=APIPath.SIGN_IN)
	public APIResponse signIn(@RequestBody @Valid SignInRequest req) throws Exception {
		String accessToken = signService.signIn(req);
		return new APIResponse(HttpStatus.OK.value(), accessToken, "S001");
	}
}