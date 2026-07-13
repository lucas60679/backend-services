package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record InviteResponse(
		@JsonProperty("convite_id") UUID conviteId,
		@JsonProperty("vaga_id") UUID vagaId,
		@JsonProperty("candidato_id") String candidatoId,
		@JsonProperty("status") String status,
		@JsonProperty("mensagem") String mensagem,
		@JsonProperty("titulo_vaga") String tituloVaga,
		@JsonProperty("descricao_vaga") String descricaoVaga,
		@JsonProperty("candidato_nome") String candidatoNome,
		@JsonProperty("candidato_email") String candidatoEmail,
		@JsonProperty("recruiter_nome") String recruiterNome,
		@JsonProperty("recruiter_email") String recruiterEmail,
		@JsonProperty("proposed_interview_at") Instant proposedInterviewAt,
		@JsonProperty("meeting_link") String meetingLink,
		@JsonProperty("schedule_status") String scheduleStatus,
		@JsonProperty("created_at") Instant createdAt,
		@JsonProperty("updated_at") Instant updatedAt
) {
}
