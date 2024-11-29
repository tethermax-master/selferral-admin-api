package io.selferral.admin.api.model.request;

import java.math.BigInteger;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteSelferralReviewRequest {
	@NotNull(message="reviewNo는 필수 입력입니다.")
	private BigInteger reviewNo;

}
