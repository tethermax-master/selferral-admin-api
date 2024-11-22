package io.selferral.admin.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.selferral.admin.api.core.APIPath;
import io.selferral.admin.api.model.response.APIResponse;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@GetMapping(path=APIPath.GET_SERVER_TIME)
	public APIResponse getTimeStamp() throws Exception {
		return new APIResponse(HttpStatus.OK.value(),System.currentTimeMillis() , "S001");
	}

}
