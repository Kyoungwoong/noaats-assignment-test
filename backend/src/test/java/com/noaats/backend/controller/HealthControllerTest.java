package com.noaats.backend.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HealthControllerTest {

	@Test
	void returnsOkWithTime() {
		HealthController controller = new HealthController();
		HealthController.HealthResponse response = controller.health();

		assertEquals("ok", response.status());
		assertNotNull(response.time());
	}
}
