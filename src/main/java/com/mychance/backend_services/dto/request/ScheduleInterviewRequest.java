package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ScheduleInterviewRequest(
		@NotNull @JsonProperty("proposed_interview_at") Instant proposedInterviewAt,
		@JsonProperty("meeting_link") String meetingLink
) {
}
