package io.selferral.admin.api.model.request;

import java.math.BigInteger;

public record UidEnrollInitRequest(
	BigInteger exchangeNo,
	String uid
	) {
	}
