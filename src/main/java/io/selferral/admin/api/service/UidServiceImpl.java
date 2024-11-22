package io.selferral.admin.api.service;

import java.math.BigInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import io.selferral.admin.api.core.CD.EnrollStatus;
import io.selferral.admin.api.core.CD.ExchangeName;
import io.selferral.admin.api.core.CD.UidEnrollProcessResponse;
import io.selferral.admin.api.mapper.ExchangeMapper;
import io.selferral.admin.api.mapper.UidEnrollMapper;
import io.selferral.admin.api.model.dto.ApiKeyDto;
import io.selferral.admin.api.model.entity.UidEnroll;
import io.selferral.admin.api.model.exchanges.Exchange;
import io.selferral.admin.api.utils.ExchangeSignUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UidServiceImpl  implements UidService{

	private final UidEnrollMapper uidEnrollMapper;
	private final ExchangeMapper exchangeMapper;
	private final RestTemplate rest;
	
	@Override
	@Transactional 
	public UidEnrollProcessResponse enrollUidProcess(BigInteger exchangeNo, String uid) throws Exception {
		// 1. 현재 진행, 완료 인 uid 인가?
		EnrollStatus status = uidEnrollMapper.getUidEnrollLastStatus(exchangeNo, uid);
		// status 타입에 따른 return 진행
		if(EnrollStatus.P.equals(status)) {
			// 진행중
			return UidEnrollProcessResponse.PROCESSING;
			
		} else if(EnrollStatus.S.equals(status)) {
			// 이미 성공
			return UidEnrollProcessResponse.ALREADY_SUCCESS;
		} else {
			// 2. api 진행
			String enName = exchangeMapper.getEnName(exchangeNo);
			ExchangeName name = ExchangeName.valueOf(enName.toUpperCase());
			ApiKeyDto api = ExchangeSignUtil.getAwsSecretKey(name);
			Exchange exchange = new Exchange(name, api, rest);
			String apiUid = exchange.getApiUid(uid);
			
			if(StringUtils.hasText(apiUid)) {
				if(ExchangeName.BITGET.equals(name) || ExchangeName.COINCATCH.equals(name)) {
					// 수동으로 진행중 insert
					uidEnrollMapper.insertUidEnroll(UidEnroll.builder().build().insert(exchangeNo, uid, EnrollStatus.P));
					return UidEnrollProcessResponse.SUCCESS;
				} else {
					// 자동 성공.
					uidEnrollMapper.insertUidEnroll(UidEnroll.builder().build().insert(exchangeNo, uid, EnrollStatus.S));
					return UidEnrollProcessResponse.SUCCESS;
				}
				
			} else {
				// 미존재 
				return UidEnrollProcessResponse.NOT_FOUND_UID;
			}
		}
	}

}
