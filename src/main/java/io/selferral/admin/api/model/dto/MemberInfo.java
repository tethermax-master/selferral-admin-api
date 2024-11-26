package io.selferral.admin.api.model.dto;

import java.math.BigInteger;

import io.selferral.admin.api.core.CD.MemberRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberInfo {

	private BigInteger memberInfoNo;
	private String email;
	private MemberRole grade;
	private String password;
}
