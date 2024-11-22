package io.selferral.admin.api.model.exchanges;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.TreeMap;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import io.selferral.admin.api.model.dto.ApiKeyDto;
import io.selferral.admin.api.utils.ExchangeSignUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bybit implements ExchangeConnect{
	private String apiKey;
	private String secretKey;
	private final String url = "https://api.bybit.com";
	private RestTemplate restTemplate;
	
	
	public Bybit(ApiKeyDto dto, RestTemplate rest) {
        // API를 통해 동적으로 값 설정
        this.apiKey = dto.getApiKey();
        this.secretKey = dto.getSecretKey();
        this.restTemplate = rest;
    }


	@Override
	public String getCustomer(String uid) throws Exception {
		String timestamp = Instant.now().toEpochMilli() + "";
		String path = "/v5/user/aff-customer-info";
		HttpHeaders headers = new HttpHeaders();
		TreeMap<String, String> req = new TreeMap<String, String>();
		req.put("uid", uid.toString());
		
		String data = timestamp + apiKey + "5000" + ExchangeSignUtil.parseParam(req, null);
		String sign;
		try {
			sign = ExchangeSignUtil.calculateHmacSHA256(data, secretKey, "bybit");
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("sign : " + sign);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-BAPI-API-KEY", apiKey);
		headers.set("X-BAPI-TIMESTAMP", timestamp);
		headers.set("X-BAPI-SIGN", sign);
		headers.set("X-BAPI-SIGN-TYPE", "2");
		headers.set("X-BAPI-RECV-WINDOW", "5000");
		
		HttpEntity<?> httpEntity = new HttpEntity<>(headers);
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(url + path)
				.queryParam("uid", uid.toString())
				.build();
		
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(uri.toString(), HttpMethod.GET, httpEntity, String.class);
		}catch (HttpClientErrorException e) {
			System.out.println("### log " + e.getMessage());
		}
		System.out.println("body : " + response.getBody());
		JSONObject resObject = new JSONObject(response.getBody());
		
		if(resObject.has("retCode")) {
			if("0".equals(resObject.get("retCode").toString())) {
				if(resObject.has("result")) {
					return resObject.getJSONObject("result").get("uid").toString();
				}
			}
		}
		return null;
	}

	
}
