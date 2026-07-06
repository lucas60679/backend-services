package com.mychance.backend_services.support;

import com.mychance.backend_services.dto.nlp.MatchingRankRequest;
import com.mychance.backend_services.service.NlpMatchingClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestConfiguration
public class TestNlpMatchingConfig {

	@Bean
	@Primary
	NlpMatchingClient testNlpMatchingClient() {
		NlpMatchingClient client = Mockito.mock(NlpMatchingClient.class);
		when(client.rankCandidates(any(MatchingRankRequest.class)))
				.thenAnswer(invocation -> NlpMatchingTestStub.fromRequest(invocation.getArgument(0)));
		return client;
	}
}
