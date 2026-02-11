package com.noaats.backend.ev;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "EV reward type")
public enum EvRewardType {
	@Schema(description = "Cash reward in KRW")
	CASH,
	@Schema(description = "Points reward (1 point = 1 KRW)")
	POINT,
	@Schema(description = "Percent discount based on base amount")
	PERCENT
}
