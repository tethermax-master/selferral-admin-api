package io.selferral.admin.api.model.request;

import java.util.Objects;

import lombok.Builder;

public record UidEnrollListRequest(
	Integer currentPage,
	Integer limit
	) {
		@Builder
		public UidEnrollListRequest{
			if(Objects.isNull(currentPage)) {
				currentPage = 1;
			}
			if(Objects.isNull(limit)) {
				limit = 10;
			}
			currentPage = (currentPage - 1) * limit;
		}
	}
