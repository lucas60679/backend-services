-- Reference schema for My Chance (PostgreSQL production target)
-- Local development uses H2 with spring.jpa.hibernate.ddl-auto=update

CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE candidates (
    id UUID PRIMARY KEY REFERENCES accounts(id)
);

CREATE TABLE anonymous_profiles (
    id UUID PRIMARY KEY,
    candidate_id UUID NOT NULL UNIQUE REFERENCES candidates(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    education_level VARCHAR(50),
    region_state VARCHAR(50),
    salary_expectation_min INT CHECK (salary_expectation_min > 0)
);

CREATE TABLE candidate_skills (
    id BIGSERIAL PRIMARY KEY,
    profile_id UUID NOT NULL REFERENCES anonymous_profiles(id),
    skill_name VARCHAR(100) NOT NULL,
    skill_level INT NOT NULL CHECK (skill_level BETWEEN 0 AND 5)
);

CREATE TABLE candidate_projects (
    id BIGSERIAL PRIMARY KEY,
    profile_id UUID NOT NULL REFERENCES anonymous_profiles(id),
    description TEXT NOT NULL
);

CREATE TABLE candidate_experiences (
    id BIGSERIAL PRIMARY KEY,
    profile_id UUID NOT NULL REFERENCES anonymous_profiles(id),
    role_title VARCHAR(150) NOT NULL,
    duration_months INT NOT NULL CHECK (duration_months > 0)
);

CREATE TABLE job_vacancies (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    recruiter_id UUID NOT NULL REFERENCES accounts(id),
    description TEXT,
    max_salary INT CHECK (max_salary > 0)
);

CREATE TABLE job_requirements (
    id BIGSERIAL PRIMARY KEY,
    job_id UUID NOT NULL REFERENCES job_vacancies(id),
    skill_name VARCHAR(100) NOT NULL,
    weight INT NOT NULL CHECK (weight BETWEEN 1 AND 5),
    is_mandatory BOOLEAN NOT NULL DEFAULT FALSE,
    min_level INT NOT NULL CHECK (min_level BETWEEN 1 AND 5)
);

CREATE TABLE interview_invites (
    id UUID PRIMARY KEY,
    job_id UUID NOT NULL REFERENCES job_vacancies(id),
    profile_id UUID NOT NULL REFERENCES anonymous_profiles(id),
    status VARCHAR(20) NOT NULL,
    message VARCHAR(500),
    proposed_interview_at TIMESTAMP,
    meeting_link VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_interview_invites_profile_status ON interview_invites(profile_id, status);
CREATE INDEX idx_interview_invites_job_id ON interview_invites(job_id);
