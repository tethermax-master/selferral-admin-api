package io.selferral.admin.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.selferral.admin.api.mapper.ExchangeMapper;
import io.selferral.admin.api.model.response.ExchangeResponse;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {

	private final ExchangeMapper exchangeListMapper; 
	
	// 거래소 목록 조회
	@Override
	public List<ExchangeResponse> getExchangeList() {
		return exchangeListMapper.getExchangeList();
	}
		
	}

