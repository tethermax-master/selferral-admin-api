package io.selferral.admin.api.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.selferral.admin.api.mapper.ExchangeListMapper;
import io.selferral.admin.api.mapper.ExchangeReviewMapper;
import io.selferral.admin.api.mapper.ExchangeTagMapper;
import io.selferral.admin.api.model.dto.ExchangeDto;
import io.selferral.admin.api.model.entity.SelferralReview;
import io.selferral.admin.api.model.request.DeleteReviewRequest;
import io.selferral.admin.api.model.request.ExchangeReivewRequest;
import io.selferral.admin.api.model.response.SelferralReviewListResponse;
import io.selferral.admin.api.model.response.SelferralReviewResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExchangeReviewServiceImpl implements ExchangeReviewService {

	private final ExchangeReviewMapper exchangeReviewMapper;
	private final ExchangeListMapper exchangeListMapper;
	private final ExchangeTagMapper exchangeTagMapper;

	@Override
	public SelferralReviewListResponse getExchangeReviewList(ExchangeReivewRequest req) {
		SelferralReviewListResponse res = new SelferralReviewListResponse();
		// Dto 안에 태킹 정보 list 추가
		List<SelferralReviewResponse> reviewList = exchangeReviewMapper.getSelferralReviewList(req);
		// reviewList 길이만큼 for 문 
		for(SelferralReviewResponse reviewData : reviewList) {
			// reviewNo 를 통해서 tag 정보를 가져오는 query 만들기
			List<String> tagList = exchangeTagMapper.getExchangeTagList(reviewData.getReviewNo());
			
			// set 해주기
			reviewData.setTagContent(tagList);
		}
		res.setSelferralList(reviewList);
		res.setTotalCount(exchangeReviewMapper.getSelferralReviewListCount(req));
		return res;
	}
	
	@Override
	public List<ExchangeDto> getExchangeList() {
		return exchangeListMapper.getExchangeList();
	}
	
	@Override
	@Transactional 
	public boolean deleteExchangeReview(DeleteReviewRequest req) throws Exception { 
		SelferralReview selferralReview = SelferralReview.builder().build().delete(req);
		boolean result = exchangeReviewMapper.deleteExchangeReview(selferralReview);
		return result;
	}
}