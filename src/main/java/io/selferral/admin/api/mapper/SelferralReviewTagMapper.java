package io.selferral.admin.api.mapper;

import java.math.BigInteger;
import java.util.List;

public interface SelferralReviewTagMapper {

	public List<String> getSelferralReviewTagList(BigInteger reviewNo);

}
