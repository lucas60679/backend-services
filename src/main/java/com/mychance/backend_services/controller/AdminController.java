package com.mychance.backend_services.controller;

import com.mychance.backend_services.config.TestScenario;
import com.mychance.backend_services.service.DemoResetService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@ConditionalOnProperty(name = "mychance.demo-reset.enabled", havingValue = "true")
public class AdminController {

	private final DemoResetService demoResetService;

	public AdminController(DemoResetService demoResetService) {
		this.demoResetService = demoResetService;
	}

	@PostMapping("/reset")
	@ResponseStatus(HttpStatus.OK)
	public Map<String, String> resetDemoData(
			@RequestParam(name = "scenario", defaultValue = "base") String scenario
	) {
		if (!TestScenario.isValidKey(scenario)) {
			throw new IllegalArgumentException("Cenário de teste inválido: " + scenario);
		}

		TestScenario selectedScenario = TestScenario.fromKey(scenario);
		demoResetService.resetToScenario(selectedScenario);

		return Map.of(
				"status", "ok",
				"scenario", selectedScenario.name().toLowerCase(),
				"message", scenarioMessage(selectedScenario)
		);
	}

	private String scenarioMessage(TestScenario scenario) {
		return switch (scenario) {
			case BASE -> "Dados restaurados ao estado base de demonstração.";
			case R1 -> "Cenário R1 pronto para recruiter@mychance.local.";
			case C2 -> "Cenário C2 pronto para ana.silva@demo.local.";
		};
	}
}
