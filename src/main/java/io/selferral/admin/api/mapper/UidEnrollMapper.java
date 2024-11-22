package io.selferral.admin.api.mapper;

import java.math.BigInteger;

import io.selferral.admin.api.core.CD.EnrollStatus;
import io.selferral.admin.api.model.entity.UidEnroll;

public interface UidEnrollMapper {
	public EnrollStatus getUidEnrollLastStatus(BigInteger exchangeNo, String uid); 
	
	public void insertUidEnroll(UidEnroll uidEnroll);
}