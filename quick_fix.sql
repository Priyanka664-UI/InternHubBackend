-- Quick fix: Add the missing column to group_invitations table
-- Connect to your PostgreSQL database and run this command:

ALTER TABLE group_invitations ADD COLUMN inviter_user_id BIGINT;
ALTER TABLE group_invitations ADD COLUMN invitee_user_id BIGINT;
ALTER TABLE group_invitations ADD COLUMN message TEXT;
ALTER TABLE group_invitations ADD COLUMN responded_at TIMESTAMP;
