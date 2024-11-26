package io.selferral.admin.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.selferral.admin.api.config.annotation.MasterAuthorize;
import io.selferral.admin.api.controller.path.APIPath;
import io.selferral.admin.api.model.response.APIResponse;

@MasterAuthorize
@RestController
@RequestMapping("/master")
public class MasterController {
	
	@GetMapping(path=APIPath.CHECK_PASSWORD) 
	public APIResponse getDashBoard() throws Exception {
		return new APIResponse(HttpStatus.OK.value(), null , "S010");
	}
}