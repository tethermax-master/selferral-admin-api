package io.selferral.admin.api.model.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UidEnrollListResponse {

	private int totalCount;
	private List<UidEnrollResponse>  uidEnrollList;

}