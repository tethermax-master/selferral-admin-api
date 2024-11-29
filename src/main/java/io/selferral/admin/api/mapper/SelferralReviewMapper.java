package io.selferral.admin.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.selferral.admin.api.model.entity.SelferralReview;
import io.selferral.admin.api.model.request.SelferralReivewRequest;
import io.selferral.admin.api.model.response.SelferralReviewResponse;

@Mapper
public interface SelferralReviewMapper {
	public List<SelferralReviewResponse> getSelferralReviewList(SelferralReivewRequest req);
	public int getSelferralReviewListCount(SelferralReivewRequest req);
	public boolean deleteSelferralReview(SelferralReview selferralReview);
}

