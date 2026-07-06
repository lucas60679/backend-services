package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SkillName {
	PYTHON("python", "Python"),
	JAVASCRIPT("javascript", "JavaScript"),
	TYPESCRIPT("typescript", "TypeScript"),
	JAVA("java", "Java"),
	KOTLIN("kotlin", "Kotlin"),
	GO("go", "Go"),
	CSHARP("csharp", "C#"),
	SQL("sql", "SQL"),
	POSTGRESQL("postgresql", "PostgreSQL"),
	MONGODB("mongodb", "MongoDB"),
	REDIS("redis", "Redis"),
	REACT("react", "React"),
	ANGULAR("angular", "Angular"),
	VUE("vue", "Vue"),
	NODEJS("nodejs", "Node.js"),
	SPRING("spring", "Spring Boot"),
	DJANGO("django", "Django"),
	FASTAPI("fastapi", "FastAPI"),
	REST_API("rest_api", "REST API"),
	GRAPHQL("graphql", "GraphQL"),
	DOCKER("docker", "Docker"),
	KUBERNETES("kubernetes", "Kubernetes"),
	TERRAFORM("terraform", "Terraform"),
	CICD("cicd", "CI/CD"),
	AWS("aws", "AWS"),
	AZURE("azure", "Azure"),
	LINUX("linux", "Linux"),
	GIT("git", "Git"),
	KAFKA("kafka", "Apache Kafka"),
	POWERBI("powerbi", "Power BI"),
	STREAMLIT("streamlit", "Streamlit"),
	MACHINE_LEARNING("machine_learning", "Machine Learning"),
	DATA_ANALYSIS("data_analysis", "Análise de Dados"),
	AGILE("agile", "Metodologias Ágeis");

	private static final Map<String, SkillName> BY_KEY = Arrays.stream(values())
			.collect(Collectors.toMap(skill -> skill.key, Function.identity()));

	private final String key;
	private final String displayName;

	SkillName(String key, String displayName) {
		this.key = key;
		this.displayName = displayName;
	}

	@JsonValue
	public String getKey() {
		return key;
	}

	public String getDisplayName() {
		return displayName;
	}

	@JsonCreator
	public static SkillName fromKey(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("Skill name cannot be blank");
		}
		SkillName skill = BY_KEY.get(value.trim().toLowerCase(Locale.ROOT));
		if (skill == null) {
			throw new IllegalArgumentException("Unknown skill: " + value);
		}
		return skill;
	}

	public static boolean isValidKey(String value) {
		return value != null && BY_KEY.containsKey(value.trim().toLowerCase(Locale.ROOT));
	}
}
