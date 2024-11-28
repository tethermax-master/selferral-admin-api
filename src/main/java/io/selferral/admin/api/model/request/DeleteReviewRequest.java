package io.selferral.admin.api.model.request;

import java.math.BigInteger;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteReviewRequest {
	// TODO class 말고 app-server 소스 참고해서 record 로 변경하세요
	@NotNull(message="입력 필수")
	private BigInteger reviewNo;
	


}
