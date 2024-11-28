package io.selferral.admin.api.model.dto;

import java.math.BigInteger;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeDto {
	 @NotNull
	 private BigInteger exchangeNo;
	 @NotNull
	 private String enName;
	
	
}
