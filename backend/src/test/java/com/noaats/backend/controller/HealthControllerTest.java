package com.noaats.backend.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HealthControllerTest {

	@Test
	void returnsOkWithTime() {
		HealthController controller = new HealthController();
		var response = controller.health();

		assertEquals(true, response.success());
		assertNotNull(response.data());
		assertEquals("ok", response.data().status());
		assertNotNull(response.data().time());
	}
}
