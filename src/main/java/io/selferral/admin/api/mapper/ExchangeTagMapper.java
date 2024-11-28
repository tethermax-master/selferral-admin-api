package io.selferral.admin.api.mapper;

import java.math.BigInteger;
import java.util.List;

public interface ExchangeTagMapper {

	List<String> getExchangeTagList(BigInteger reviewNo);

}
