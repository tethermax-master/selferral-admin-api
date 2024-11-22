package io.selferral.admin.api.model.exchanges;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

public class OKX implements ExchangeConnect {
	private String apiKey;
	private String secretKey;
	private String passphrase;
	private final String url = "https://www.okx.com";
	private RestTemplate restTemplate;
	
	
	public OKX(ApiKeyDto dto, RestTemplate rest) {
        // API를 통해 동적으로 값 설정
        this.apiKey = dto.getApiKey();
        this.secretKey = dto.getSecretKey();
        this.passphrase = dto.getPassphrase();
        this.restTemplate = rest;
    }


	@Override
	public String getCustomer(String uid) throws Exception {
		Instant instant = Instant.now();
		DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX") // 또는 "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
				.withZone(ZoneId.of("UTC"));
		
		String isoTimestamp = formatter.format(instant);
		String path = "/api/v5/affiliate/invitee/detail";
		TreeMap<String, String> req = new TreeMap<String, String>();
		req.put("uid", uid.toString());
		
		String data = isoTimestamp + "GET" + path + "?" + ExchangeSignUtil.parseParam(req, null);
		String signature;
		try {
			signature = ExchangeSignUtil.calculateHmacSHA256(data, secretKey, "okx");
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("OK-ACCESS-KEY", apiKey);
		headers.set("OK-ACCESS-SIGN", signature);
		headers.set("OK-ACCESS-TIMESTAMP", isoTimestamp);
		headers.set("OK-ACCESS-PASSPHRASE", passphrase);
		
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
		System.out.println("2. body = " + response.getBody());
		JSONObject resObject = new JSONObject(response.getBody());
		if(resObject.has("code")) {
			if("0".equals(resObject.get("code").toString())) {
				return uid;
			} 
		} 
		
		return null;
	}
}
