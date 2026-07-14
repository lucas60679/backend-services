package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LanguageProficiencyResponse(
		@JsonProperty("idioma") String idioma,
		@JsonProperty("nivel") String nivel
) {
}
