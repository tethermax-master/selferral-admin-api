package io.selferral.admin.api.service;

import io.selferral.admin.api.model.request.UidEnrollListRequest;
import io.selferral.admin.api.model.request.UpdateUidEnrollRequest;
import io.selferral.admin.api.model.response.UidEnrollListResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UidEnrollService {
	public UidEnrollListResponse getUidEnrollList(UidEnrollListRequest req) throws Exception;
	public void uidEnrollListExcelDownload(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public boolean updateUidEnroll(UpdateUidEnrollRequest req) throws Exception;
}
