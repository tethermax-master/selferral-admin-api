package io.selferral.admin.api.model.exchanges;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.selferral.admin.api.model.dto.ApiKeyDto;
import io.selferral.admin.api.utils.ExchangeSignUtil;

public class CoinCatch implements ExchangeConnect{
	private String apiKey;
	private String secretKey;
	private String passphrase;
	private final String url = "https://api.coincatch.com";
	private RestTemplate restTemplate;
	
	public CoinCatch(ApiKeyDto dto, RestTemplate rest) {
        // API를 통해 동적으로 값 설정
        this.apiKey = dto.getApiKey();
        this.secretKey = dto.getSecretKey();
        this.passphrase = dto.getPassphrase();
        this.restTemplate = rest;
    }
	
	@Override
	public String getCustomer(String uid) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String timestamp = Instant.now().toEpochMilli() + "";
		TreeMap<String, String> req = new TreeMap<String, String>();
		req.put("uid", uid.toString());
		req.put("startTime", "0");
		req.put("endTime", timestamp);
		
		System.out.println("uid : " + uid);
		String path = "/api/broker/v1/agent/customerList";
		String data;
		try {
			data = timestamp + "POST" + path + objectMapper.writeValueAsString(req);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		} 
		//+ "?" + paramStr;
		
		String signature;
		try {
			signature = ExchangeSignUtil.calculateHmacSHA256(data , secretKey, "coincatch");
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("ACCESS-KEY", apiKey);
		headers.set("ACCESS-SIGN", signature);
		headers.set("ACCESS-TIMESTAMP", timestamp);
		headers.set("ACCESS-PASSPHRASE", passphrase);
		HttpEntity<?> httpEntity = new HttpEntity<>(req, headers);
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		for (Map.Entry<String, String> entry : req.entrySet()) {
			multiValueMap.add(entry.getKey(), entry.getValue());
		}
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(url + path)
				.build();
		
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(uri.toString(), HttpMethod.POST, httpEntity, String.class);
		}catch (HttpClientErrorException e) {
			System.out.println("### log " + e.getMessage());
			return null;
		}
		System.out.println("body : " + response.getBody());
		JSONObject resObject = new JSONObject(response.getBody());
		if(resObject.has("data")) {
			if(resObject.optJSONArray("data") != null) {
				JSONArray dataArray = resObject.getJSONArray("data");
				if(dataArray.length() > 0) {
					return dataArray.getJSONObject(0).get("uid").toString();
				}
				
			}
		}
			
		return null;
	}

}
