
--creates the database
CREATE DATABASE a3database;

--connects to the new database
\c a3database;

--creates the table within database
CREATE TABLE IF NOT EXISTS students (
    student_id serial PRIMARY KEY, 
    first_name VARCHAR(255) NOT NULL, 
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL, 
    enrollment_date DATE
);

--sets initial data
INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES 
('John', 'Doe', 'john.doe@example.com', '2023-09-01'), 
('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01'), 
('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02');