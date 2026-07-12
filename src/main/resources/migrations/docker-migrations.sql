-- Idempotent migrations for existing Docker volumes (prod profile uses ddl-auto=validate).
ALTER TABLE interview_invites ADD COLUMN IF NOT EXISTS proposed_interview_at TIMESTAMP;
ALTER TABLE interview_invites ADD COLUMN IF NOT EXISTS meeting_link VARCHAR(500);
