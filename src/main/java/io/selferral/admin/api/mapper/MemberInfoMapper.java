
package io.selferral.admin.api.mapper;

import io.selferral.admin.api.model.dto.MemberInfo;

public interface MemberInfoMapper {
	public MemberInfo getMemberInfo(String email);
}