package io.selferral.admin.api.service;

import java.util.List;

import io.selferral.admin.api.model.dto.ExchangeDto;

public interface ExchangeListService {

	// 거래소 목록 조회
	 List<ExchangeDto> getExchangeList();
}
