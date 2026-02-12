package com.noaats.backend.api;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.noaats.backend.auth.UserPrincipal;
import com.noaats.backend.dto.auth.AuthRequestDto;
import com.noaats.backend.dto.auth.AuthResponseDto;
import com.noaats.backend.dto.ev.EvRequestDto;
import com.noaats.backend.dto.ev.EvResponseDto;
import com.noaats.backend.dto.ev.EvScenarioDto;
import com.noaats.backend.dto.ev.EvScenarioResultDto;
import com.noaats.backend.ev.EvRewardType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApiModelTest {

	@Test
	void buildsApiErrorResponse() {
		ApiErrorResponse response = ApiErrorResponse.from(ErrorCode.BAD_REQUEST, null, List.of(), "/api/test");

		assertEquals(false, response.success());
		assertEquals(ErrorCode.BAD_REQUEST.name(), response.code());
		assertEquals(ErrorCode.BAD_REQUEST.defaultMessage(), response.message());
		assertEquals("/api/test", response.path());
		assertNotNull(response.timestamp());
	}

	@Test
	void initializesSchemasAndRecords() {
		ApiEnvelopeSchema<String> envelope = new ApiEnvelopeSchema<>();
		envelope.success = true;
		envelope.data = "ok";
		ApiErrorResponseSchema errorSchema = new ApiErrorResponseSchema();
		FieldErrorDetail detail = new FieldErrorDetail("field", "message");
		UserPrincipal principal = new UserPrincipal(1L, "user");
		AuthRequestDto authRequest = new AuthRequestDto("user", "pass");
		AuthResponseDto authResponse = new AuthResponseDto("token");
		EvScenarioDto scenario = new EvScenarioDto("label", 0.5, EvRewardType.POINT, 1000L);
		EvScenarioResultDto scenarioResult = new EvScenarioResultDto("label", 0.5, 1000L, 500L);
		EvRequestDto evRequest = new EvRequestDto(10_000L, List.of(scenario));
		EvResponseDto evResponse = new EvResponseDto(500L, 0.05, List.of(scenarioResult));

		assertEquals("ok", envelope.data);
		assertEquals("field", detail.field());
		assertEquals("user", principal.username());
		assertEquals("user", authRequest.username());
		assertEquals("token", authResponse.accessToken());
		assertEquals(10_000L, evRequest.baseAmount());
		assertEquals(500L, evResponse.expectedValue());
	}
}
