package com.mychance.backend_services.support;

import com.mychance.backend_services.domain.entity.Account;
import com.mychance.backend_services.domain.entity.Candidate;
import com.mychance.backend_services.domain.enums.UserRole;

public final class AccountTestBuilder {

	private AccountTestBuilder() {
	}

	public static Account candidateAccount(String email) {
		return new Account("Candidato Teste", email, "encoded-password", UserRole.CANDIDATE);
	}

	public static Candidate candidateFrom(Account account) {
		return new Candidate(account);
	}
}
