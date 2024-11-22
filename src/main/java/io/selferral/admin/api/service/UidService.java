package io.selferral.admin.api.service;

import java.math.BigInteger;

import io.selferral.admin.api.core.CD.UidEnrollProcessResponse;

public interface UidService {
	public UidEnrollProcessResponse enrollUidProcess(BigInteger exchangeNo, String uid) throws Exception; 
}
