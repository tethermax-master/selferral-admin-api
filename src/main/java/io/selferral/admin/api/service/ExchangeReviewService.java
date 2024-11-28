package io.selferral.admin.api.service;

import java.util.List;

import io.selferral.admin.api.model.dto.ExchangeDto;
import io.selferral.admin.api.model.request.DeleteReviewRequest;
import io.selferral.admin.api.model.request.ExchangeReivewRequest;
import io.selferral.admin.api.model.response.SelferralReviewListResponse;

public interface ExchangeReviewService {
	public SelferralReviewListResponse getExchangeReviewList(ExchangeReivewRequest req);
	public List<ExchangeDto> getExchangeList();
	boolean deleteExchangeReview(DeleteReviewRequest req) throws Exception;
	
	
}