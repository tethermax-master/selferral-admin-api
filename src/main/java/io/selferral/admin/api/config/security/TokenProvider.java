package io.selferral.admin.api.config.security;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenProvider {
    
	@Value("${JWT}") //토큰 생성시 필요한 secretKey
	private String secretKey;
	
	@Value("${issuer}")
	private String issuer;

	private static final int webExpireTime = 24; // ADMIN 24시간
	
	public String createToken(String userSpecification) {
		return Jwts.builder()
				.signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
	    		.setSubject(userSpecification)
	        	.setIssuer(issuer)
	        	.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusHours(webExpireTime)))
	        	.compact();
	}
	
	public String validateTokenAndGetSubject(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey.getBytes())
			    .build()
			    .parseClaimsJws(token)
			    .getBody()
			    .getSubject();
    }
}