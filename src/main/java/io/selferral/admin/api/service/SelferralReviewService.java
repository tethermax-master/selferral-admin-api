package io.selferral.admin.api.service;

import java.util.List;

import io.selferral.admin.api.model.request.DeleteSelferralReviewRequest;
import io.selferral.admin.api.model.request.SelferralReivewRequest;
import io.selferral.admin.api.model.response.ExchangeResponse;
import io.selferral.admin.api.model.response.SelferralReviewListResponse;

public interface SelferralReviewService {
	public List<ExchangeResponse> getExchangeList() throws Exception;
	public SelferralReviewListResponse getSelferralReviewList(SelferralReivewRequest req) throws Exception;
	public boolean deleteSelferralReview(DeleteSelferralReviewRequest req) throws Exception;
	
	
}