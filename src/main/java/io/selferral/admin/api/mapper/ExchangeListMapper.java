package io.selferral.admin.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.selferral.admin.api.model.dto.ExchangeDto;

public interface ExchangeListMapper {

	List<ExchangeDto> getExchangeList();
	
	
}
