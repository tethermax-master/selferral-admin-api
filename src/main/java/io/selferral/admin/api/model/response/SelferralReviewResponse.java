package io.selferral.admin.api.model.response;

import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelferralReviewResponse {
	private BigInteger reviewNo;
	private String exchangeName;
	private String uid;
	private String content;
	private String deleteYn;
	private String createDt;
	private String deleteDt;
	private List<String> tagContent;
	
}