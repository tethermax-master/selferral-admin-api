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

public class BitMart implements ExchangeConnect {
	private String apiKey;
	private String secretKey;
	private String passphrase;
//	private final String url = "https://api-cloud.bitmart.com";
	private final String url = "https://api-cloud-v2.bitmart.com";
	private RestTemplate restTemplate;
	
	
	public BitMart(ApiKeyDto dto, RestTemplate rest) {
        // API를 통해 동적으로 값 설정
        this.apiKey = dto.getApiKey();
        this.secretKey = dto.getSecretKey();
        this.passphrase = dto.getPassphrase();
        this.restTemplate = rest;
    }


	@Override
	public String getCustomer(String uid) throws Exception {
		String path = "/contract/private/affiliate/rebate-list";
		String timestamp = Instant.now().toEpochMilli() + "";
		
		TreeMap<String, String> req = new TreeMap<String, String>();
		req.put("page", "1");
		req.put("user_id", uid.toString());
		req.put("size", "1000");
		req.put("currency", "USDT");
		String paramStr = ExchangeSignUtil.parseParam(req, null);
		String data = timestamp + "#" + passphrase + "#" +  paramStr;
		String signature = null;
		try {
			signature = ExchangeSignUtil.calculateHmacSHA256(data, secretKey, "bitmart");
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-BM-KEY", apiKey);
		headers.set("X-BM-SIGN", signature);
		headers.set("X-BM-TIMESTAMP", timestamp);
		
		HttpEntity<?> httpEntity = new HttpEntity<>(headers);
		
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		for (Map.Entry<String, String> entry : req.entrySet()) {
			multiValueMap.add(entry.getKey(), entry.getValue());
		}
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(url + path)
				.queryParams(multiValueMap)
				.build();
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(uri.toString(), HttpMethod.GET, httpEntity, String.class);
		} catch (HttpClientErrorException e) {
			System.out.println("### log " + e.getMessage());
		}
		System.out.println("2. body = " + response.getBody());
		JSONObject jObject = new JSONObject(response.getBody());
		if(jObject.has("data")) {
			JSONObject dataObject = new JSONObject(jObject.get("data").toString());
			if(dataObject.has("rebate_detail_page_data")) {
				JSONArray jArray = dataObject.getJSONArray("rebate_detail_page_data");
				
				if(jArray.length() > 0 ) {
					for(int i = 0; i < jArray.length(); i++) {
						String tradeUserId = jArray.getJSONObject(i).get("trade_user_id").toString();
						return tradeUserId;
					}
				} 
			} 
		} 
		return null;
	}
	
}
