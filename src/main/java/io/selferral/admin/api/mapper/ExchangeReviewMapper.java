package io.selferral.admin.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.selferral.admin.api.model.entity.SelferralReview;
import io.selferral.admin.api.model.request.ExchangeReivewRequest;
import io.selferral.admin.api.model.response.SelferralReviewResponse;

@Mapper
public interface ExchangeReviewMapper {
	// 거래소 후기 리스트 조회(검색,태깅)
	public List<SelferralReviewResponse> getSelferralReviewList(ExchangeReivewRequest req);
	public int getSelferralReviewListCount(ExchangeReivewRequest req);
	// 특정 리뷰의 삭제 여부를 업데이트
	public boolean deleteExchangeReview(SelferralReview selferralReview);
}

