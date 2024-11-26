package io.selferral.admin.api.service;

import io.selferral.admin.api.model.request.SignInRequest;

public interface SignService {
	public String signIn(SignInRequest req) throws Exception;
}
