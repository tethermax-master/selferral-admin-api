
package io.selferral.admin.api.mapper;

import java.math.BigInteger;
import java.util.List;

import io.selferral.admin.api.model.dto.UidEnrollList;
import io.selferral.admin.api.model.entity.UidEnroll;
import io.selferral.admin.api.model.request.UidEnrollListRequest;
import io.selferral.admin.api.model.response.UidEnrollResponse;

public interface UidEnrollMapper {
	public int getUidEnrollTodayCount();
	public int getUidEnrollDaysAgoCount(int days);
	public int getUidEnrollSuccessTotalCount();
	public List<UidEnrollList> getUidListExcelDownload();
	public int getUidEnrollListCount(UidEnrollListRequest req);
	public List<UidEnrollResponse> getUidEnrollList(UidEnrollListRequest req);
	public boolean isProccessingUidEnroll(BigInteger enrollNo);
	
	public boolean updateUidEnroll(UidEnroll uidEnroll);
}