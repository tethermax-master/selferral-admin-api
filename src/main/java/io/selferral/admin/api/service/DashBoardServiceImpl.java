package io.selferral.admin.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.selferral.admin.api.mapper.UidEnrollMapper;
import io.selferral.admin.api.model.response.DashBoardResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService{
	
	@Autowired
	UidEnrollMapper uidEnrollMapper;
	
	@Override
	public DashBoardResponse getDashBoardInfo() throws Exception {
		DashBoardResponse res = new DashBoardResponse();
		res.setTodayRegistCount(uidEnrollMapper.getUidEnrollTodayCount());
		res.setLast7DaysRegistCount(uidEnrollMapper.getUidEnrollDaysAgoCount(6));
		res.setAccumulatedRegistCount(uidEnrollMapper.getUidEnrollSuccessTotalCount());
		return res;
	}
}
