package io.selferral.admin.api.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.BinaryUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.selferral.admin.api.model.request.PreSignedPostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Util {
	private final AmazonS3 amazonS3;
	private AWSCredentials awsCredentials = DefaultAWSCredentialsProviderChain.getInstance().getCredentials();

	private String useOnlyOneFileName;
	@Value("${cloud.aws.s3.bucket}")
    private String bucket;
	@Value("${cloud.aws.region.static}")
    private String region;
	@Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    // 버킷 리스트를 가져오는 메서드이다.
	public List<Bucket> getBucketList() {
		return amazonS3.listBuckets();
	}

	// 버킷을 생성하는 메서드이다.
	public Bucket createBucket(String bucketName) {
		return amazonS3.createBucket(bucketName);
	}

	// 폴더 생성 (폴더는 파일명 뒤에 "/"를 붙여야한다.)
	public void createFolder(String bucketName, String folderName) {
		amazonS3.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
	}

	// 파일 업로드
	public void fileUpload(String bucketName, String fileName, byte[] fileData) throws FileNotFoundException {

		String filePath = (fileName).replace(File.separatorChar, '/'); // 파일 구별자를 `/`로 설정(\->/) 이게 기존에 / 였어도 넘어오면서 \로 바뀌는 거같다.
		ObjectMetadata metaData = new ObjectMetadata();

		metaData.setContentLength(fileData.length);   //메타데이터 설정 -->원래는 128kB까지 업로드 가능했으나 파일크기만큼 버퍼를 설정시켰다.
	    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData); //파일 넣음

	    amazonS3.putObject(bucketName, filePath, byteArrayInputStream, metaData);

	}

	// 파일 삭제
	public void fileDelete(String bucketName, String fileName) {
		String imgName = (fileName).replace(File.separatorChar, '/');
		amazonS3.deleteObject(bucketName, imgName);
		System.out.println("삭제성공");
	}

	// 파일 URL
	public String getFileURL(String bucketName, String fileName) {
		System.out.println("넘어오는 파일명 : "+fileName);
		String imgName = (fileName).replace(File.separatorChar, '/');
		return amazonS3.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, imgName)).toString();
	}
	
	public List<String> getFileListByContains(String bucketName, String prefix, String fileName){
		List<String> res = new ArrayList<String>();
		ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName, prefix);
		for (S3ObjectSummary s3 : result.getObjectSummaries()) {
			String name = s3.getKey().substring(s3.getKey().lastIndexOf("/") + 1);
			if(name.contains(fileName)) {
				res.add(name);
			}
		}
		return res;
	}
	
	public String getPreSignedUrl(String fileName, String method, long expTime, String contentType, long contentLength) {
		String res = new String();
		String onlyOneFileName = fileName;

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(onlyOneFileName, HttpMethod.valueOf(method), expTime, contentType, contentLength);
        try {
        	res = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return res;
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String fileName, HttpMethod method, long expTime, String contentType, long contentLength) {
    	System.out.println("method=" + method);
    	
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(method)
                        .withExpiration(getPreSignedUrlExpiration(expTime));
//                        .withSSEAlgorithm(SSEAlgorithm.AES256);
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        
//        if(HttpMethod.PUT.equals(method)) {
//        	generatePresignedUrlRequest.putCustomRequestHeader(Headers.CONTENT_TYPE, contentType);
//        	generatePresignedUrlRequest.putCustomRequestHeader(Headers.CONTENT_LENGTH, contentLength + "");
//        }
        
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration(long expTime) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * expTime;	// 만료주기 10초
        expiration.setTime(expTimeMillis);
        log.info(expiration.toString());
        return expiration;
    }

    public String findByName(String path) {
//        if (!amazonS3.doesObjectExist(bucket,editPath+ useOnlyOneFileName))
//            return "File does not exist";
        log.info("Generating signed URL for file name {}", useOnlyOneFileName);
//        return  amazonS3.getUrl(bucket,editPath+useOnlyOneFileName).toString();
        System.out.println("bucket=" + bucket + ", region" + region + ", useOnlyOneFileName" + useOnlyOneFileName);
        return "https://"+bucket+".s3."+region+".amazonaws.com/"+path+"/"+useOnlyOneFileName;
    }
    
    public PreSignedPostRequest generatePresignedPost(String objectKey, long fileMinSize, long fileMaxSize, long expTime) throws Exception {
    	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
    	dateTimeFormat.setTimeZone(new SimpleTimeZone(0, "UTC"));
    	SimpleDateFormat dateStampFormat = new SimpleDateFormat("yyyyMMdd");
    	dateStampFormat.setTimeZone(new SimpleTimeZone(0, "UTC"));
    	Date now = new Date();
    	String dateTimeStamp = dateTimeFormat.format(now);
    	String dateStamp = dateStampFormat.format(now);
    	Map<String, Object> policy = generatePolicy(fileMaxSize, fileMinSize, objectKey, dateTimeStamp, dateStamp, expTime);

    	String encodedPolicy = encodePolicy(policy);
    	String signature = sign(encodedPolicy, dateStamp, "s3");
    	PreSignedPostRequest result = buildSignedPostRequest(encodedPolicy, signature, dateTimeStamp,dateStamp, objectKey);
    	
    	String preSignedUrl = result.getUrl() + "?";
    	for (Map.Entry<String, String> entry : result.getBody().entrySet()) {
    	    String key = entry.getKey();
    	    String value = entry.getValue();
    	    preSignedUrl = String.format(preSignedUrl + "%s=%s&", key, value);
    	}
    	System.out.println(preSignedUrl);
    	return result;

    }
    
    private String encodePolicy(Map<String, Object> policy) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        String policyAsStr = mapper.writeValueAsString(policy);
        byte[] policyAsBytes = policyAsStr.getBytes(StandardCharsets.UTF_8);

        return BinaryUtils.toBase64(policyAsBytes);
    }
    
    public String sign(String toSign, String dateStamp, String service) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        byte[] kSecret = ("AWS4" + secretKey).getBytes(StandardCharsets.UTF_8);
        byte[] kDate = hmacSHA256(dateStamp, kSecret);
        byte[] kRegion = hmacSHA256(region, kDate);
        byte[] kService = hmacSHA256(service, kRegion);
        byte[] kSigning = hmacSHA256("aws4_request", kService);
        byte[] signature = hmacSHA256(toSign, kSigning);

        return BinaryUtils.toHex(signature);
    }
    
    private byte[] hmacSHA256(String data, byte[] key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }
    
    private PreSignedPostRequest buildSignedPostRequest(String encodedPolicy, String signature, String dateTimeStamp, String dateStamp, String objectKey) {
        Map<String, String> body = generateAwsDefaultParams(objectKey, dateTimeStamp, dateStamp);
        body.put("x-amz-signature", signature);
        body.put("policy", encodedPolicy);

        String url = String.format("https://%s.s3.amazonaws.com/%s", bucket, objectKey);

        return PreSignedPostRequest.builder().body(body).url(url).build();
    }
    
    private Map<String, String> generateAwsDefaultParams(String objectKey, String dateTimeStamp, String dateStamp) {
        String scope = String.format("%s/%s/%s/%s/%s", accessKey, dateStamp, region, "s3", "aws4_request");
        Map<String, String> params = new HashMap<>();
        params.put("x-amz-algorithm", "AWS4-HMAC-SHA256");
        params.put("x-amz-credential", scope);
        params.put("x-amz-date", dateTimeStamp);
//        params.put("key", objectKey);

        System.out.println(awsCredentials + ", " + (this.awsCredentials instanceof AWSSessionCredentials));
        if (this.awsCredentials instanceof AWSSessionCredentials) {
            params.put("x-amz-security-token", ((AWSSessionCredentials) this.awsCredentials).getSessionToken());
        }

        return params;
    }
    
    private Map<String, Object> generatePolicy(long fileMaxSize, long fileMinSize, String objectKey, String dateTimeStamp, String dateStamp, long expTime) {
        List<?> conditions = generateConditions(objectKey, fileMaxSize, fileMinSize, dateTimeStamp, dateStamp);
        return Map.of(
                "expiration", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS'Z'").format(getPreSignedUrlExpiration(expTime)),
                "conditions", conditions
        );
    }
    
    private List<?> generateConditions(String objectKey, long fileMaxSize, long fileMinSize, String dateTimeStamp, String dateStamp) {
        List<?> contentLengthRange = List.of("content-length-range", fileMinSize, fileMaxSize);

        List<Object> conditions = new LinkedList<>();
        conditions.add(contentLengthRange);
        Map<String, String> awsDefaults = generateAwsDefaultParams(objectKey, dateTimeStamp, dateStamp);

        conditions.addAll(awsDefaults.entrySet());

        return conditions;
    }
}