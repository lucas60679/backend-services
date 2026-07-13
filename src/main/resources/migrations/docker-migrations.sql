-- Idempotent migrations for existing Docker volumes (prod profile uses ddl-auto=validate).
ALTER TABLE interview_invites ADD COLUMN IF NOT EXISTS proposed_interview_at TIMESTAMP;
ALTER TABLE interview_invites ADD COLUMN IF NOT EXISTS meeting_link VARCHAR(500);
ALTER TABLE interview_invites ADD COLUMN IF NOT EXISTS schedule_status VARCHAR(30);

UPDATE interview_invites
SET schedule_status = 'PROPOSED_BY_RECRUITER'
WHERE status = 'ACEITO'
  AND proposed_interview_at IS NOT NULL
  AND schedule_status IS NULL;

UPDATE interview_invites
SET schedule_status = 'AWAITING_SCHEDULE'
WHERE status = 'ACEITO'
  AND schedule_status IS NULL;
