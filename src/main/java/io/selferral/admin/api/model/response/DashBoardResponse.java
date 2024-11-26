package io.selferral.admin.api.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashBoardResponse {
	private int todayRegistCount;
	private int Last7DaysRegistCount;
	private int accumulatedCount;
	
}