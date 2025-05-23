# **MySQL Setup Guide for Hotel Booking System**

------------------------
Step 1: Log in to MySQL
------------------------

mysql -u root -p

------------------------
Step 2: Create the Database
------------------------

CREATE DATABASE hotel_booking;
USE hotel_booking;

------------------------
Step 3: Create Tables
------------------------

### Creating USER Table
CREATE TABLE users (
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(50) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
role VARCHAR(20) NOT NULL
);

### Creating ROOM Table
CREATE TABLE rooms (
room_number INT NOT NULL PRIMARY KEY,
bed_type VARCHAR(20),
price DOUBLE,
status VARCHAR(20)
);

### Creating RESERVATION Table
CREATE TABLE reservations (
reservation_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
room_number INT NOT NULL,
guest_name VARCHAR(100) NOT NULL,
phone VARCHAR(20) NOT NULL,
email VARCHAR(100),
check_in DATETIME NOT NULL,
check_out DATETIME NOT NULL,
bed_type VARCHAR(50),
FOREIGN KEY (room_number) REFERENCES rooms(room_number)
);

------------------------
Step 4: Insert Dummy Data
------------------------

### Inserting into USER Table
INSERT INTO users (username, password, role) VALUES
('admin', 'admin', 'Admin'),
('0987654321', 'user', 'User'),
('1234567890', 'user', 'User');

### Inserting into ROOM Table
INSERT INTO rooms (room_number, bed_type, price, status) VALUES
(101, 'Single', 1500, 'Occupied'),
(102, 'Double', 1850, 'Occupied'),
(103, 'Single', 2200, 'Occupied'),
(104, 'Double', 2550, 'Available'),
(105, 'Suite', 2900, 'Occupied'),
(106, 'Single', 3250, 'Available'),
(107, 'Double', 3600, 'Available'),
(108, 'Suite', 3950, 'Occupied'),
(109, 'Single', 4300, 'Available'),
(110, 'Double', 4650, 'Occupied'),
(111, 'Suite', 5000, 'Available'),
(112, 'Single', 5500, 'Available');

### Inserting into RESERVATION Table
INSERT INTO reservations (room_number, guest_name, phone, email, check_in, check_out,
bed_type) VALUES
(101, 'Anjali Mehra', '9876543210', NULL, '2025-05-24 14:00:00', '2025-05-26 11:00:00', 'Single'),
(102, 'Rohan Singh', '9123456789', NULL, '2025-05-25 15:00:00', '2025-05-27 10:30:00', 'Double'),
(103, 'Neha Patel', '9001234567', NULL, '2025-05-23 12:00:00', '2025-05-24 09:00:00', 'Deluxe'),
(104, 'Amit Desai', '9988776655', NULL, '2025-05-22 13:00:00', '2025-05-25 10:00:00', 'Double'),
(105, 'Sara Khan', '8899001122', NULL, '2025-05-24 14:30:00', '2025-05-28 12:00:00', 'Single'),
(106, 'kasim', '1234567890', NULL, '2025-05-23 00:00:00', '2025-05-24 00:00:00', NULL),
(101, 'user', '0987654321', NULL, '2025-05-23 00:00:00', '2025-05-24 00:00:00', NULL),
(103, 'mohit', '0987654321', NULL, '2025-05-23 00:00:00', '2025-05-27 00:00:00', NULL);

------------------------
Step 5: Verify Everything
------------------------

SHOW TABLES;

SELECT * FROM users;
SELECT * FROM rooms;
SELECT * FROM reservations;