package io.selferral.admin.api.model.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelferralReviewListResponse {
	private int totalCount;
	private List<SelferralReviewResponse> selferralList;
	
}
