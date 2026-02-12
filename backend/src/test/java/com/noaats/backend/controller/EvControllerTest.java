package com.noaats.backend.controller;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.noaats.backend.dto.ev.EvRequestDto;
import com.noaats.backend.dto.ev.EvScenarioDto;
import com.noaats.backend.ev.EvRewardType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EvControllerTest {

	@Test
	void calculatesExpectedValue() {
		EvController controller = new EvController();
		EvRequestDto request = new EvRequestDto(
			10_000L,
			List.of(new EvScenarioDto("Test", 0.5, EvRewardType.POINT, 1000L))
		);

		var response = controller.calculate(request);

		assertEquals(true, response.success());
		assertNotNull(response.data());
		assertEquals(500L, response.data().expectedValue());
	}
}
