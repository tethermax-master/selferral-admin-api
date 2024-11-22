package io.selferral.admin.api.model.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import io.selferral.admin.api.core.CD.EnrollStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor (access = AccessLevel.PRIVATE)
public class UidEnroll {
	private BigInteger enrollNo;
	private BigInteger exchangeNo;
	private String uid;
	private EnrollStatus status;
	private LocalDateTime createDt;
	private LocalDateTime updateDt;
	
	public UidEnroll insert(BigInteger exchangeNo, String uid, EnrollStatus status) {
		return UidEnroll.builder()
						.exchangeNo(exchangeNo)
						.uid(uid)
						.status(status)
						.createDt(LocalDateTime.now())
						.updateDt(LocalDateTime.now())
						.build();
	}
	
	
	@Builder
	public UidEnroll(BigInteger enrollNo, BigInteger exchangeNo, String uid, EnrollStatus status,
			LocalDateTime createDt, LocalDateTime updateDt) {
		super();
		this.enrollNo = enrollNo;
		this.exchangeNo = exchangeNo;
		this.uid = uid;
		this.status = status;
		this.createDt = createDt;
		this.updateDt = updateDt;
	}
}