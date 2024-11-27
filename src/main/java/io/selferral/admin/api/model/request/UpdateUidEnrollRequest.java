package io.selferral.admin.api.model.request;

import java.math.BigInteger;

import io.selferral.admin.api.core.CD.UidEnrollConfirmStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateUidEnrollRequest(
	@NotNull(message = "enrollNo는 필수입니다.")
    BigInteger enrollNo,
    @NotNull(message = "status는 필수입니다.")
    UidEnrollConfirmStatus status
) {
}