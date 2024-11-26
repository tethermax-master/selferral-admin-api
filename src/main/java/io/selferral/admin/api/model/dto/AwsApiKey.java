package io.selferral.admin.api.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwsApiKey {
	String apiKey;
	String secretKey;
	String passphrase;
}
