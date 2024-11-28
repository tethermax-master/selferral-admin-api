package io.selferral.admin.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.selferral.admin.api.mapper.ExchangeListMapper;
import io.selferral.admin.api.model.dto.ExchangeDto;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ExchangeListServiceImpl implements ExchangeListService {

	private final ExchangeListMapper exchangeListMapper; 
	
	// 거래소 목록 조회
	@Override
	public List<ExchangeDto> getExchangeList() {
		return exchangeListMapper.getExchangeList();
	}
		
	}

