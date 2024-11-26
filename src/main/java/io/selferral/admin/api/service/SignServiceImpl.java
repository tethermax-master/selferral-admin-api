package io.selferral.admin.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import io.selferral.admin.api.config.security.TokenProvider;
import io.selferral.admin.api.core.CD.ErrorCode;
import io.selferral.admin.api.core.exception.TetherMaxException;
import io.selferral.admin.api.core.util.AES256Util;
import io.selferral.admin.api.mapper.MemberInfoMapper;
import io.selferral.admin.api.model.dto.MemberInfo;
import io.selferral.admin.api.model.request.SignInRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class SignServiceImpl implements SignService{
	
	private final PasswordEncoder encoder;
	private final TokenProvider tokenProvider;
	
	@Autowired
	MemberInfoMapper memberInfoMapper;
	
	@Override
	public String signIn(SignInRequest req) throws Exception {
		String decPassword = null;
		boolean isValidPass = true;
		MemberInfo memberInfo = memberInfoMapper.getMemberInfo(req.email());
		if(ObjectUtils.isEmpty(memberInfo)) {
			throw new TetherMaxException(ErrorCode.NOT_REGISTERED_MEMBER);
		}
		
		log.info("req.password() : " + req.password());
		try { // 패스워드 암호화 위변조 체크
			decPassword = AES256Util.AES_Decode(req.password());
		} catch (Exception e) {
			isValidPass = false;
		}
		
		if(!isValidPass || !encoder.matches(decPassword, memberInfo.getPassword())) {
			throw new TetherMaxException(ErrorCode.INVALID_ID_OR_PASSWORD);
		}
		String accessToken = tokenProvider.createToken(String.format("%s:%s", memberInfo.getMemberInfoNo(), memberInfo.getGrade()));
		return accessToken;
	}
}
