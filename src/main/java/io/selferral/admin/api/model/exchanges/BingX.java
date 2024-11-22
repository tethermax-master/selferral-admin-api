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

import io.selferral.admin.api.model.dto.ApiKeyDto;
import io.selferral.admin.api.utils.ExchangeSignUtil;

public class BingX implements ExchangeConnect{
	private String apiKey;
	private String secretKey;
	private final String url = "https://open-api.bingx.com";
	private RestTemplate restTemplate;
	
	
	public BingX(ApiKeyDto dto, RestTemplate rest) {
        // API를 통해 동적으로 값 설정
        this.apiKey = dto.getApiKey();
        this.secretKey = dto.getSecretKey();
        this.restTemplate = rest;
    }


	@Override
	public String getCustomer(String uid) throws Exception {
		//기간 30일의 uid 검색 미존재 
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-BX-APIKEY", apiKey);
		int pageIndex = 1;
		String path = "/openApi/agent/v1/account/inviteAccountList";
		while(true) {
			String timestamp = Instant.now().toEpochMilli() + "";
			
			TreeMap<String, String> req = new TreeMap<String, String>();
			req.put("pageIndex", pageIndex+"");
			req.put("pageSize", "100");
			req.put("recvWindow", "0");
			
			String signature;
			try {
				signature = ExchangeSignUtil.calculateHmacSHA256(ExchangeSignUtil.parseParam(req, timestamp), secretKey, "bingx");
			} catch (InvalidKeyException | NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			}
			
			HttpEntity<?> httpEntity = new HttpEntity<>(headers);
			
			MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
			for (Map.Entry<String, String> entry : req.entrySet()) {
				multiValueMap.add(entry.getKey(), entry.getValue());
			}
			UriComponents uri = UriComponentsBuilder.fromHttpUrl(url + path)
					.queryParams(multiValueMap)
					.queryParam("timestamp", timestamp)
					.queryParam("signature", signature)
					
					.build();
			
			ResponseEntity<String> response = null;
			try {
				response = restTemplate.exchange(uri.toString(), HttpMethod.GET, httpEntity, String.class);
			}catch (HttpClientErrorException e) {
				e.printStackTrace();
				return null;
			}
			System.out.println("body : " + response.getBody());
			JSONObject resObject = new JSONObject(response.getBody());
			if(resObject.has("data")) {
				pageIndex++;
				JSONArray list = resObject.getJSONObject("data").getJSONArray("list");
				for(int i = 0; i < list.length(); i++) {
					if(uid.equals(list.getJSONObject(i).get("uid").toString())){
						return list.getJSONObject(i).get("uid").toString();
					}
				}
			} else {
				break;
			}
		}
		return null;
	}
}
