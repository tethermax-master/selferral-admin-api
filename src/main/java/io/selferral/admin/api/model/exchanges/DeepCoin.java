package io.selferral.admin.api.model.exchanges;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class DeepCoin implements ExchangeConnect{
	private String apiKey;
	private String secretKey;
	private String passphrase;
	private final String url = "https://api.deepcoin.com";
	private RestTemplate restTemplate;
	
	
	public DeepCoin(ApiKeyDto dto, RestTemplate rest) {
        // API를 통해 동적으로 값 설정
        this.apiKey = dto.getApiKey();
        this.secretKey = dto.getSecretKey();
        this.passphrase = dto.getPassphrase();
        this.restTemplate = rest;
    }


	@Override
	public String getCustomer(String uid) throws Exception {
		String path = "/deepcoin/agents/users";
		Instant instant = Instant.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC")); // 또는 "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
		
		TreeMap<String, String> req = new TreeMap<String, String>();
		req.put("uid", uid.toString());
		
		String isoTimestamp = formatter.format(instant);
		String paramStr = ExchangeSignUtil.parseParam(req, null);
		String data =  isoTimestamp + "GET" + path + "?" + paramStr ;
		String signature = null;
		try {
			signature = ExchangeSignUtil.calculateHmacSHA256(data , secretKey, "deepcoin");
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("DC-ACCESS-KEY", apiKey);
		headers.set("DC-ACCESS-SIGN", signature);
		headers.set("DC-ACCESS-PASSPHRASE", passphrase);
		headers.set("DC-ACCESS-TIMESTAMP", isoTimestamp);
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		for (Map.Entry<String, String> entry : req.entrySet()) {
			multiValueMap.add(entry.getKey(), entry.getValue());
		}
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(url + path)
							.queryParams(multiValueMap)
							.build();
		
		ResponseEntity<String> response = null;
		HttpEntity<?> httpEntity = new HttpEntity<>(headers);
		try {
			response = restTemplate.exchange(uri.toString(), HttpMethod.GET, httpEntity, String.class);
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
		}
//		System.out.println("2. body = " + response.getBody());
		
		JSONObject jObj = new JSONObject(response.getBody());
		if(jObj.has("data")) {
			
			JSONObject dataObj = jObj.getJSONObject("data");
			JSONArray userList = dataObj.getJSONArray("list");
			JSONObject resultObj = userList.getJSONObject(0);
			String resultUid = resultObj.get("uid").toString();
			return resultUid;
		}
		return null;
	}
	
}
