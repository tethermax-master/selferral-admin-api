package io.selferral.admin.api.model.response;

import java.math.BigInteger;
import java.time.ZonedDateTime;

import io.selferral.admin.api.core.CD.UidEnrollStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UidEnrollResponse {

	private BigInteger enrollNo;
	private String exchangeName;
	private BigInteger uid;
	private UidEnrollStatus status;
	private ZonedDateTime createDt;
	private ZonedDateTime updateDt;

	public String getUid() {
		return this.uid.toString();
	}
}