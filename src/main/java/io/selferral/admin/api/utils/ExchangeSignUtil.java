package io.selferral.admin.api.utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;

import io.selferral.admin.api.core.CD.ExchangeName;
import io.selferral.admin.api.model.dto.ApiKeyDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExchangeSignUtil {
	
	public static ApiKeyDto getAwsSecretKey(ExchangeName name) {
		String secretName = "prod/seferral/exchange";
	    String endpoint = "secretsmanager.ap-northeast-2.amazonaws.com";
	    String region = "ap-northeast-2";
	    AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
	    AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
	    clientBuilder.setEndpointConfiguration(config);
	    AWSSecretsManager client = clientBuilder.build();
	    
	    String secret;
	    ByteBuffer binarySecretData;
	    GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
	            .withSecretId(secretName).withVersionStage("AWSCURRENT");
	    GetSecretValueResult getSecretValueResult = null;
	    try {
	        getSecretValueResult = client.getSecretValue(getSecretValueRequest);
	      } catch(ResourceNotFoundException e) {
	        System.out.println("The requested secret " + secretName + " was not found");
	    } catch (InvalidRequestException e) {
	        System.out.println("The request was invalid due to: " + e.getMessage());
	    } catch (InvalidParameterException e) {
	        System.out.println("The request had invalid params: " + e.getMessage());
	    }
	      if(getSecretValueResult == null) {
	        return null;
	    }
	      // Depending on whether the secret was a string or binary, one of these fields will be populated
	    if(getSecretValueResult.getSecretString() != null) {
	        secret = getSecretValueResult.getSecretString();
	        JSONObject json = new JSONObject(secret);
	        ApiKeyDto res = new ApiKeyDto();
	        if(json.has("kr-api-key-" + name)) {
	        	res.setApiKey(json.getString("kr-api-key-" + name));
	        }
	        if(json.has("kr-secret-key-" + name)) {
	        	res.setSecretKey(json.getString("kr-secret-key-" + name));
	        }
	        if(json.has("kr-passphrase-key-" + name)) {
	        	res.setPassphrase(json.getString("kr-passphrase-key-" + name));
	        }
	        return res;
	    }else {
	        binarySecretData = getSecretValueResult.getSecretBinary();
	        System.out.println(binarySecretData.toString());
	    }
		return null;
	}
	
	public static String calculateHmacSHA256(String data, String key,String type) throws NoSuchAlgorithmException, InvalidKeyException {
		String HmacSha = "HmacSHA256";
		if("gateIo".equals(type)) {
			HmacSha = "HmacSHA512";
		}
        Mac sha256Hmac = Mac.getInstance(HmacSha);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HmacSha);
        sha256Hmac.init(secretKey);
        byte[] rawHmac = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        if("bybit".equals(type) || "bingx".equals(type) || "bitvenus".equals(type) || "bitmart".equals(type) || "bitmex".equals(type) || "wooX".equals(type) || "zoomex".equals(type) || "mexc".equals(type) || "gateIo".equals(type)) {
        	String res = byte2hex(rawHmac);
        	return res;
        } else if("blofin".equals(type)) {
//        	System.out.println("1 : " + java.util.Base64.getEncoder().encodeToString((new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key.getBytes(StandardCharsets.UTF_8))).hmacHex(data).getBytes()));
        	String res = byte2hex(rawHmac);
        	return Base64.getEncoder().encodeToString(res.getBytes());
        } else {
        	return Base64.getEncoder().encodeToString(rawHmac);
        }
    }
	
	public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        for (int n = 0; b != null && n < b.length; n++) {
            String temp = Integer.toHexString(b[n] & 0XFF);
            if (temp.length() == 1) {
                hs.append('0');
            }
            hs.append(temp);
        }
        return hs.toString();
    }
    
	public static String bytesToHex(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String res = formatter.toString();
        formatter.close();
        return res;
    }

	public static String parseParam(Map<String, String> paramsMap, String timestamp) {
        // TreeMap을 사용하여 키를 정렬
        TreeMap<String, String> sortedParams = new TreeMap<>(paramsMap);

        StringBuilder paramsStr = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = sortedParams.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            paramsStr.append(entry.getKey()).append("=").append(entry.getValue());
            
            if (iterator.hasNext()) {
                // 현재 반복이 마지막이 아니라면 "&"를 추가
                paramsStr.append("&");
            } else {
            	if(timestamp != null) {
            		paramsStr.append("&timestamp=" + timestamp);
            	}
            }
        }
//        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
//            paramsStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
//        }


        return paramsStr.toString();
    }

	public static String parseParamObjectValue(Map<String, Object> paramsMap, String timestamp) {
        // TreeMap을 사용하여 키를 정렬
        TreeMap<String, Object> sortedParams = new TreeMap<>(paramsMap);

        StringBuilder paramsStr = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iterator = sortedParams.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            paramsStr.append(entry.getKey()).append("=").append(entry.getValue());
            
            if (iterator.hasNext()) {
                // 현재 반복이 마지막이 아니라면 "&"를 추가
                paramsStr.append("&");
            } else {
            	if(timestamp != null) {
            		paramsStr.append("&timestamp=" + timestamp);
            	}
            }
        }
//        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
//            paramsStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
//        }


        return paramsStr.toString();
    }
    
	public static String padTimestamp(long timestamp, int desiredLength) {
        String timestampString = Long.toString(timestamp);

        // 현재 길이가 이미 원하는 길이 이상이면 그대로 반환
        if (timestampString.length() >= desiredLength) {
            return timestampString;
        }

        // 부족한 길이만큼 0으로 채우기
        StringBuilder paddedTimestamp = new StringBuilder(timestampString);
        while (paddedTimestamp.length() < desiredLength) {
            paddedTimestamp.append("0");
        }

        return paddedTimestamp.toString();
		
		
	}
}
