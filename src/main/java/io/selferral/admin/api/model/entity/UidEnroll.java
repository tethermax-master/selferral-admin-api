package io.selferral.admin.api.model.entity;

import java.math.BigInteger;

import io.selferral.admin.api.core.CD.UidEnrollConfirmStatus;
import io.selferral.admin.api.core.CD.UidEnrollStatus;
import io.selferral.admin.api.model.request.UpdateUidEnrollRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor (access = AccessLevel.PRIVATE)
public class UidEnroll {
	
	private BigInteger enrollNo;
	private String exchangeName;
	private BigInteger uid;
	private UidEnrollStatus status;

	public UidEnroll update(UpdateUidEnrollRequest req) {
		return UidEnroll.builder()
				.enrollNo(req.enrollNo())
				.status(UidEnrollConfirmStatus.SUCCESS.equals(req.status()) ? UidEnrollStatus.S : UidEnrollStatus.R)
				.build();
	}

	@Builder
	public UidEnroll(BigInteger enrollNo, String exchangeName, BigInteger uid, UidEnrollStatus status) {
		super();
		this.enrollNo = enrollNo;
		this.exchangeName = exchangeName;
		this.uid = uid;
		this.status = status;
	}

}