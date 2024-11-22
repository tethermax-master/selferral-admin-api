package io.selferral.admin.api.model.request;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreSignedPostRequest {
	Map<String, String> body;
	String url;
	
	@Builder
	public PreSignedPostRequest(Map<String, String> body, String url) {
		super();
		this.body = body;
		this.url = url;
	}
}
