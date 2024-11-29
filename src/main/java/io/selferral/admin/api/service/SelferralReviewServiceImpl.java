package io.selferral.admin.api.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.selferral.admin.api.mapper.ExchangeMapper;
import io.selferral.admin.api.mapper.SelferralReviewMapper;
import io.selferral.admin.api.mapper.SelferralReviewTagMapper;
import io.selferral.admin.api.model.entity.SelferralReview;
import io.selferral.admin.api.model.request.DeleteSelferralReviewRequest;
import io.selferral.admin.api.model.request.SelferralReivewRequest;
import io.selferral.admin.api.model.response.ExchangeResponse;
import io.selferral.admin.api.model.response.SelferralReviewListResponse;
import io.selferral.admin.api.model.response.SelferralReviewResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SelferralReviewServiceImpl implements SelferralReviewService {

	private final SelferralReviewMapper selferralReviewMapper;
	private final ExchangeMapper exchangeMapper;
	private final SelferralReviewTagMapper selferralReviewTagMapper;

	@Override
	public List<ExchangeResponse> getExchangeList() throws Exception {
		return exchangeMapper.getExchangeList();
	}
	
	@Override
	public SelferralReviewListResponse getSelferralReviewList(SelferralReivewRequest req) throws Exception {
		SelferralReviewListResponse res = new SelferralReviewListResponse();
		// Dto 안에 태킹 정보 list 추가
		List<SelferralReviewResponse> reviewList = selferralReviewMapper.getSelferralReviewList(req);
		for(SelferralReviewResponse reviewData : reviewList) {
			List<String> tagList = selferralReviewTagMapper.getSelferralReviewTagList(reviewData.getReviewNo());
			reviewData.setTagContent(tagList);
		}
		res.setSelferralList(reviewList);
		res.setTotalCount(selferralReviewMapper.getSelferralReviewListCount(req));
		return res;
	}
	
	@Override
	@Transactional 
	public boolean deleteSelferralReview(DeleteSelferralReviewRequest req) throws Exception { 
		SelferralReview selferralReview = SelferralReview.builder().build().delete(req);
		return selferralReviewMapper.deleteSelferralReview(selferralReview);
	}
}