-- Drop existing trigger and function for updating last inspection time if they exist
DROP TRIGGER IF EXISTS trg_update_vehicle_last_inspection_time ON vehicles--EOS
DROP FUNCTION IF EXISTS update_vehicle_last_inspection_time()--EOS

-- Create or replace the function to update the last_inspection_time column
-- This function will be executed by a trigger.
CREATE OR REPLACE FUNCTION update_vehicle_last_inspection_time()
RETURNS TRIGGER AS $$
BEGIN
  -- Set the last_inspection_time of the new row to the current timestamp
  NEW.last_inspection_time := NOW(); -- Internal semicolons are fine
RETURN NEW;
END;
$$
LANGUAGE plpgsql--EOS -- Apply separator after LANGUAGE directive

-- Create the trigger that fires before an UPDATE on the vehicles table
-- It executes the update_vehicle_last_inspection_time function for each row affected
CREATE TRIGGER trg_update_vehicle_last_inspection_time
    BEFORE UPDATE ON vehicles -- Corrected table name
    FOR EACH ROW
    EXECUTE FUNCTION update_vehicle_last_inspection_time()--EOS


-- Create the vehicle_insert_audit table if it does not exist
-- This table will store a record whenever a new vehicle is inserted.
CREATE TABLE IF NOT EXISTS vehicle_insert_audit (
                                                    audit_id SERIAL PRIMARY KEY, -- Auto-incrementing primary key
                                                    vehicle_id UUID NOT NULL,    -- ID of the vehicle that was inserted
                                                    inserted_at TIMESTAMP DEFAULT NOW() -- Timestamp of the insertion
    )--EOS

-- Drop existing trigger and function for logging vehicle inserts if they exist
DROP TRIGGER IF EXISTS trg_log_vehicle_insert ON vehicles--EOS
DROP FUNCTION IF EXISTS log_vehicle_insert()--EOS

-- Create or replace the function to log vehicle inserts into the audit table
-- This function will be executed by a trigger.
CREATE OR REPLACE FUNCTION log_vehicle_insert()
RETURNS TRIGGER AS $$
BEGIN
  -- Insert the ID of the newly inserted vehicle into the audit table
  -- Assuming the 'vehicles' table has an 'id' column that corresponds to vehicle_id
INSERT INTO vehicle_insert_audit(vehicle_id)
VALUES (NEW.id);
-- Return the new row; required for AFTER triggers, though the return value is ignored
RETURN NEW;
END;
$$
LANGUAGE plpgsql--EOS -- Apply separator after LANGUAGE directive

-- Create the trigger that fires after an INSERT on the vehicles table
CREATE TRIGGER trg_log_vehicle_insert
    AFTER INSERT ON vehicles -- Corrected table name
    FOR EACH ROW
    EXECUTE FUNCTION log_vehicle_insert()--EOS
