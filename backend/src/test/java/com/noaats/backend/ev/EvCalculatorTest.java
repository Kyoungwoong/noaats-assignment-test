package com.noaats.backend.ev;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.noaats.backend.dto.ev.EvScenarioDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EvCalculatorTest {

	@Test
	void calculates_expected_value_for_mixed_rewards() {
		List<EvScenarioDto> scenarios = List.of(
			new EvScenarioDto("cash", 0.1, EvRewardType.CASH, 1000),
			new EvScenarioDto("point", 0.2, EvRewardType.POINT, 500),
			new EvScenarioDto("percent", 0.05, EvRewardType.PERCENT, 10)
		);

		EvCalculator.EvResult result = EvCalculator.calculate(10_000L, scenarios);

		// 0.1*1000 + 0.2*500 + 0.05*(1000)
		assertEquals(100 + 100 + 50, result.expectedValue());
		assertEquals(0.025, result.expectedDiscountRate(), 0.0001);
	}
}
