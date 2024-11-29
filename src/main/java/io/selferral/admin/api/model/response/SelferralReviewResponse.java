package io.selferral.admin.api.model.response;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;

import io.selferral.admin.api.core.CD.DeleteYn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelferralReviewResponse {
	private BigInteger reviewNo;
	private String exchangeName;
	private BigInteger uid;
	private String content;
	private DeleteYn deleteYn;
	private ZonedDateTime createDt;
	private ZonedDateTime deleteDt;
	private List<String> tagContent;

	public String getUid() {
		return this.uid.toString();
	}
}