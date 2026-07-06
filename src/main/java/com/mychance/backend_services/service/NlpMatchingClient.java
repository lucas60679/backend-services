package com.mychance.backend_services.service;

import com.mychance.backend_services.dto.nlp.MatchingRankRequest;
import com.mychance.backend_services.dto.nlp.MatchingRankResponse;
import com.mychance.backend_services.exception.NlpMatchingUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class NlpMatchingClient {

	private final RestClient restClient;
	private final boolean enabled;

	public NlpMatchingClient(
			RestClient.Builder restClientBuilder,
			@Value("${mychance.nlp.base-url:http://localhost:8000}") String baseUrl,
			@Value("${mychance.nlp.enabled:true}") boolean enabled
	) {
		this.restClient = restClientBuilder.baseUrl(baseUrl).build();
		this.enabled = enabled;
	}

	public MatchingRankResponse rankCandidates(MatchingRankRequest request) {
		if (!enabled) {
			throw new NlpMatchingUnavailableException("NLP matching service is disabled");
		}

		try {
			MatchingRankResponse response = restClient.post()
					.uri("/api/v1/matching/rank")
					.contentType(MediaType.APPLICATION_JSON)
					.body(request)
					.retrieve()
					.body(MatchingRankResponse.class);

			if (response == null || response.ranking() == null) {
				throw new NlpMatchingUnavailableException("NLP matching service returned an empty response");
			}

			return response;
		}
		catch (RestClientException exception) {
			throw new NlpMatchingUnavailableException(
					"NLP matching service is unavailable",
					exception
			);
		}
	}
}
