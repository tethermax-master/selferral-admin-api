package io.selferral.admin.api.model.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import io.selferral.admin.api.core.CD.DeleteYn;
import io.selferral.admin.api.model.request.DeleteReviewRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class SelferralReview {
	private BigInteger reviewNo;
	private BigInteger exchangeNo;
    private String exchangeName;
    private String status;
    private DeleteYn deleteYn;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    
    public SelferralReview delete(DeleteReviewRequest req) {
        return SelferralReview.builder()
                .reviewNo(req.getReviewNo())             // 수정할 리뷰 번호
                .deleteYn(DeleteYn.Y)                 // 새로운 삭제 여부 값
                .build();
    }

    @Builder
	public SelferralReview(BigInteger reviewNo, BigInteger exchangeNo, String exchangeName, String status, DeleteYn deleteYn,
			LocalDateTime createDt, LocalDateTime updateDt) {
		super();
		this.reviewNo = reviewNo;
		this.exchangeNo = exchangeNo;
		this.exchangeName = exchangeName;
		this.status = status;
		this.deleteYn = deleteYn;
		this.createDt = createDt;
		this.updateDt = updateDt;
	}
    
}
