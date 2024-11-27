package io.selferral.admin.api.model.dto;

import java.math.BigInteger;
import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UidEnrollList {

	private BigInteger uid;
	private String exchangeName;
	private ZonedDateTime updateDt;

	public String getUid() {
		return this.uid.toString();
	}
}
