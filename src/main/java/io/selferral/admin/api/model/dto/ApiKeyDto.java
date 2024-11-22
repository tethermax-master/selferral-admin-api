package io.selferral.admin.api.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiKeyDto {

	private String apiKey;
	private String secretKey;
	private String passphrase;
}
