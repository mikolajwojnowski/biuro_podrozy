-- Drop existing tables if they exist
DROP TABLE IF EXISTS reservation_participants;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS trips;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email VARCHAR(60) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role BOOLEAN NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT 1
);

-- Create trips table
CREATE TABLE trips (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    trip_date DATE NOT NULL,
    capacity INTEGER NOT NULL,
    days INTEGER,
    price REAL,
    available_spots INTEGER NOT NULL DEFAULT 0,
    isActive BOOLEAN NOT NULL DEFAULT 1
);

-- Create reservations table
CREATE TABLE reservations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50),
    email VARCHAR(60),
    phone_number VARCHAR(15),
    trip_id INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT 1,
    numberOfPeople INTEGER NOT NULL,
    totalPrice DECIMAL(38,2) NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (trip_id) REFERENCES trips(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create reservation_participants table
CREATE TABLE reservation_participants (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    reservation_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50),
    FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_user_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_trip_active ON trips(isActive);
CREATE INDEX IF NOT EXISTS idx_reservation_trip ON reservations(trip_id);
CREATE INDEX IF NOT EXISTS idx_reservation_user ON reservations(user_id);
CREATE INDEX IF NOT EXISTS idx_reservation_active ON reservations(active);
CREATE INDEX IF NOT EXISTS idx_participant_reservation ON reservation_participants(reservation_id); 