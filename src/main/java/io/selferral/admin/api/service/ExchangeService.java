package io.selferral.admin.api.service;

import java.util.List;

import io.selferral.admin.api.model.response.ExchangeResponse;

public interface ExchangeService {

	// 거래소 목록 조회
	 List<ExchangeResponse> getExchangeList();
}
