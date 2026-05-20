CREATE DATABASE ArtConnect;
USE ArtConnect;

CREATE TABLE Artist (
    artist_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    bio TEXT,
    birth_year SMALLINT,
    contact_email VARCHAR(150) UNIQUE,
    phone VARCHAR(30),
    city VARCHAR(100),
    website VARCHAR(200),
    social_media VARCHAR(200),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Discipline (
    discipline_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Artist_Discipline (
    artist_id INT NOT NULL,
    discipline_id INT NOT NULL,
    PRIMARY KEY (artist_id, discipline_id),
    FOREIGN KEY (artist_id) REFERENCES Artist(artist_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (discipline_id) REFERENCES Discipline(discipline_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Artwork (
    artwork_id INT AUTO_INCREMENT PRIMARY KEY,
    artist_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    creation_year SMALLINT,
    type VARCHAR(100),
    medium VARCHAR(100),
    dimensions VARCHAR(100),
    description TEXT,
    price DECIMAL(12,2) NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES Artist(artist_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT chk_artwork_price CHECK (price >= 0),
    CONSTRAINT chk_artwork_status CHECK (status IN ('FOR_SALE', 'SOLD', 'EXHIBITED'))
);

CREATE TABLE ArtworkTag (
    tag_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Artwork_Tag (
    artwork_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (artwork_id, tag_id),
    FOREIGN KEY (artwork_id) REFERENCES Artwork(artwork_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES ArtworkTag(tag_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Gallery (
    gallery_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(200),
    owner_name VARCHAR(100),
    opening_hours VARCHAR(100),
    contact_phone VARCHAR(30),
    rating DECIMAL(2,1) DEFAULT 0,
    website VARCHAR(200),
    CONSTRAINT chk_gallery_rating CHECK (rating >= 0 AND rating <= 5)
);

CREATE TABLE Exhibition (
    exhibition_id INT AUTO_INCREMENT PRIMARY KEY,
    gallery_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    description TEXT,
    curator_name VARCHAR(100),
    theme VARCHAR(150),
    FOREIGN KEY (gallery_id) REFERENCES Gallery(gallery_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT chk_exhibition_dates CHECK (end_date IS NULL OR end_date >= start_date)
);

CREATE TABLE Exhibition_Artwork (
    exhibition_id INT NOT NULL,
    artwork_id INT NOT NULL,
    PRIMARY KEY (exhibition_id, artwork_id),
    FOREIGN KEY (exhibition_id) REFERENCES Exhibition(exhibition_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (artwork_id) REFERENCES Artwork(artwork_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Workshop (
    workshop_id INT AUTO_INCREMENT PRIMARY KEY,
    instructor_artist_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    date DATETIME NOT NULL,
    duration_minutes INT,
    max_participants INT,
    price DECIMAL(10,2) NOT NULL DEFAULT 0,
    location VARCHAR(150),
    description TEXT,
    level VARCHAR(30),
    FOREIGN KEY (instructor_artist_id) REFERENCES Artist(artist_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT chk_workshop_price CHECK (price >= 0),
    CONSTRAINT chk_workshop_duration CHECK (duration_minutes IS NULL OR duration_minutes > 0),
    CONSTRAINT chk_workshop_maxparticipants CHECK (max_participants IS NULL OR max_participants > 0),
    CONSTRAINT chk_workshop_level CHECK (level IS NULL OR level IN ('beginner', 'intermediate', 'advanced'))
);

CREATE TABLE CommunityMember (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    birth_year SMALLINT,
    phone VARCHAR(30),
    city VARCHAR(100),
    membership_type VARCHAR(20),
    CONSTRAINT chk_membership_type CHECK (membership_type IS NULL OR membership_type IN ('free', 'premium'))
);

CREATE TABLE Member_Favorite_Discipline (
    member_id INT NOT NULL,
    discipline_id INT NOT NULL,
    PRIMARY KEY (member_id, discipline_id),
    FOREIGN KEY (member_id) REFERENCES CommunityMember(member_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (discipline_id) REFERENCES Discipline(discipline_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    workshop_id INT NOT NULL,
    member_id INT NOT NULL,
    booking_date DATETIME NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (workshop_id) REFERENCES Workshop(workshop_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES CommunityMember(member_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT uq_booking UNIQUE (workshop_id, member_id),
    CONSTRAINT chk_payment_status CHECK (payment_status IN ('PENDING', 'PAID', 'CANCELLED'))
);

CREATE TABLE Review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    artwork_id INT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    review_date DATE NOT NULL,
    FOREIGN KEY (member_id) REFERENCES CommunityMember(member_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (artwork_id) REFERENCES Artwork(artwork_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT uq_review UNIQUE (member_id, artwork_id),
    CONSTRAINT chk_review_rating CHECK (rating BETWEEN 1 AND 5)
);

