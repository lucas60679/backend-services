package com.mychance.backend_services.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class TextSanitizerServiceTest {

	private final TextSanitizerService sanitizer = new TextSanitizerService();

	@Test
	void removesEmailAndUrlsFromProjectDescription() {
		String input = "Meu perfil: jose@email.com e https://linkedin.com/in/jose";
		String result = sanitizer.sanitize(input);

		assertThat(result).contains("[email removido]");
		assertThat(result).contains("[link removido]");
		assertThat(result).doesNotContain("jose@email.com");
	}

	@Test
	void removesGithubLinksWithoutHttpPrefix() {
		String result = sanitizer.sanitize("Repositório em github.com/usuario/projeto");

		assertThat(result).contains("[link removido]");
		assertThat(result).doesNotContain("github.com");
	}

	@Test
	void keepsCleanDescriptionsUntouched() {
		String input = "Desenvolvimento de API REST com Spring Boot e Docker.";

		assertThat(sanitizer.sanitize(input)).isEqualTo(input);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"   "})
	void returnsBlankInputAsIs(String input) {
		assertThat(sanitizer.sanitize(input)).isEqualTo(input);
	}
}
