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
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import io.selferral.admin.api.model.dto.ApiKeyDto;
import io.selferral.admin.api.utils.ExchangeSignUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MEXC implements ExchangeConnect {
	private String apiKey;
	private String secretKey;
	private final String url = "https://api.mexc.com";
	private RestTemplate restTemplate;
	
	
	public MEXC(ApiKeyDto dto, RestTemplate rest) {
        // API를 통해 동적으로 값 설정
        this.apiKey = dto.getApiKey();
        this.secretKey = dto.getSecretKey();
        this.restTemplate = rest;
    }
	
	@Override
	public String getCustomer(String uid) throws Exception {
		String timestamp = Instant.now().toEpochMilli() + "";
		String path = "/api/v3/rebate/affiliate/referral";
		
		TreeMap<String, String> req = new TreeMap<String, String>();
		req.put("uid", uid.toString());
		req.put("timestamp", timestamp);
		
		String paramStr = ExchangeSignUtil.parseParam(req, null);
		String signature = null;
		try {
			signature = ExchangeSignUtil.calculateHmacSHA256(paramStr, secretKey, "mexc");
			req.put("signature", signature);
			log.info("signature = " + signature);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-MEXC-APIKEY", apiKey);
		
		HttpEntity<?> httpEntity = new HttpEntity<>(headers);
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		for (Map.Entry<String, String> entry : req.entrySet()) {
			multiValueMap.add(entry.getKey(), entry.getValue());
		}
		System.out.println("multiValueMap : " + multiValueMap);
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(url + path)
				.queryParams(multiValueMap)
				.build();
		
		ResponseEntity<String> response = null;
		//{"ret_code":0,"ret_msg":"OK","ext_code":"","result":{"is_valid":1,"signup_time":"2024-07-30 06:04:26 +0000 UTC"},"ext_info":null,"time_now":1723178427390}
		try {
			response = restTemplate.exchange(uri.toString(), HttpMethod.GET, httpEntity, String.class);
			System.out.println(response.getBody().toString());
		} catch (Exception e) {
			System.out.println("### log " + e.getMessage());
		}
		JSONObject jsonObject = new JSONObject(response.getBody());
		if(!ObjectUtils.isEmpty(jsonObject)) {
			if(jsonObject.has("code") && jsonObject.has("success")) {
				if("0".equals(jsonObject.get("code").toString()) && Boolean.parseBoolean(jsonObject.get("success").toString())) {
					JSONArray resultList = jsonObject.getJSONObject("data").getJSONArray("resultList");
					if(resultList.length() > 0) {
						return resultList.getJSONObject(0).get("uid").toString();
					}
				}
			}
		} 
		return null;
	}
}
