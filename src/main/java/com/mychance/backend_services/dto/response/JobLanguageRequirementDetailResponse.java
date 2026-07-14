package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JobLanguageRequirementDetailResponse(
		@JsonProperty("idioma") String idioma,
		@JsonProperty("nivel_min") String nivelMin
) {
}
