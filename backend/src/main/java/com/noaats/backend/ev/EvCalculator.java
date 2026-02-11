package com.noaats.backend.ev;

import java.util.ArrayList;
import java.util.List;

import com.noaats.backend.dto.ev.EvScenarioDto;
import com.noaats.backend.dto.ev.EvScenarioResultDto;

public final class EvCalculator {
	private EvCalculator() {}

	public static EvResult calculate(long baseAmount, List<EvScenarioDto> scenarios) {
		List<EvScenarioResultDto> results = new ArrayList<>();
		double expectedValue = 0.0;

		if (scenarios != null) {
			for (EvScenarioDto scenario : scenarios) {
				long rewardValue = normalizeReward(baseAmount, scenario);
				double scenarioEv = scenario.probability() * rewardValue;
				expectedValue += scenarioEv;
				results.add(new EvScenarioResultDto(
					scenario.label(),
					scenario.probability(),
					rewardValue,
					roundCurrency(scenarioEv)
				));
			}
		}

		long expectedValueRounded = roundCurrency(expectedValue);
		double discountRate = baseAmount > 0 ? (double) expectedValueRounded / baseAmount : 0.0;
		return new EvResult(expectedValueRounded, discountRate, results);
	}

	private static long normalizeReward(long baseAmount, EvScenarioDto scenario) {
		if (scenario.rewardType() == EvRewardType.PERCENT) {
			return Math.round(baseAmount * (scenario.rewardValue() / 100.0));
		}
		// CASH and POINT are treated as KRW
		return scenario.rewardValue();
	}

	private static long roundCurrency(double value) {
		return (long) Math.floor(value);
	}

	public record EvResult(long expectedValue, double expectedDiscountRate, List<EvScenarioResultDto> scenarios) {}
}
