USE ArtConnect;
-- Prompt:Generate realistic SQL INSERT statements for an ArtConnect database with the following tables: Artist, Discipline, Artist_Discipline, Artwork, ArtworkTag, Artwork_Tag, Gallery, Exhibition, Exhibition_Artwork, Workshop, CommunityMember, Member_Favorite_Discipline, Booking, and Review.
-- Include multiple artists, artworks, exhibitions, workshops, members, tags, disciplines, bookings, and reviews.
-- Make the data coherent, with valid foreign keys and realistic values for names, cities, titles, dates, prices, levels, ratings, and statuses.


INSERT INTO Discipline (name) VALUES
('Painting'),
('Sculpture'),
('Photography'),
('Digital Art'),
('Mixed Media');

INSERT INTO Artist (name, bio, birth_year, contact_email, phone, city, website, social_media, is_active) VALUES
('Leonardo Vinci', 'Classical and modern visual artist.', 1980, 'leo@artconnect.com', '111111111', 'Florence', 'www.leovinci.com', '@leoart', TRUE),
('Claude Monet', 'Landscape painter and impressionist specialist.', 1975, 'claude@artconnect.com', '222222222', 'Paris', 'www.claudemonet.com', '@claudepaints', TRUE),
('Auguste Rodin', 'Sculptor known for expressive forms.', 1968, 'rodin@artconnect.com', '333333333', 'Paris', 'www.rodinart.com', '@rodinsculpt', TRUE);

INSERT INTO Artist_Discipline VALUES
(1,1),
(2,1),
(3,2);

INSERT INTO Artwork (artist_id, title, creation_year, type, medium, dimensions, description, price, status) VALUES
(1, 'Golden Silence', 2020, 'Painting', 'Oil on Canvas', '100x80 cm', 'A calm classical composition.', 2500.00, 'FOR_SALE'),
(2, 'Light of Spring', 2021, 'Painting', 'Watercolor', '70x50 cm', 'Landscape inspired by nature.', 1800.00, 'EXHIBITED'),
(3, 'Stone Emotion', 2019, 'Sculpture', 'Marble', '150x60 cm', 'Expressive marble sculpture.', 5000.00, 'FOR_SALE');

INSERT INTO ArtworkTag (name) VALUES
('Modern'),
('Classic'),
('Nature'),
('Portrait'),
('Abstract');

INSERT INTO Artwork_Tag VALUES
(1,2),
(1,4),
(2,3),
(3,1);

INSERT INTO Gallery (name, address, owner_name, opening_hours, contact_phone, rating, website) VALUES
('Louvre Art House', 'Rue de Rivoli, Paris', 'Marie Laurent', '09:00-18:00', '444444444', 4.9, 'www.louvrearthouse.com'),
('Metropolitan Hub', '100 Fifth Ave, New York', 'John Carter', '10:00-19:00', '555555555', 4.8, 'www.metropolitanhub.com');

INSERT INTO Exhibition (gallery_id, title, start_date, end_date, description, curator_name, theme) VALUES
(1, 'Renaissance Revival', '2026-03-14', '2026-04-20', 'Exhibition inspired by renaissance techniques.', 'Anna Smith', 'Classic Renaissance'),
(2, 'Impressionist Dreams', '2026-02-14', '2026-03-30', 'Color and light in modern interpretation.', 'David Lee', 'Light and Color');

INSERT INTO Exhibition_Artwork VALUES
(1,1),
(2,2),
(2,3);

INSERT INTO Workshop (instructor_artist_id, title, date, duration_minutes, max_participants, price, location, description, level) VALUES
(1, 'Mastering Oil Painting', '2026-04-19 10:00:00', 180, 20, 150.00, 'Paris Studio A', 'Oil painting fundamentals and advanced methods.', 'intermediate'),
(2, 'Impressionist Landscapes', '2026-04-24 11:00:00', 150, 15, 120.00, 'Paris Studio B', 'Painting landscapes with impressionist style.', 'beginner'),
(3, 'Sculpting Modernity', '2026-04-29 09:00:00', 200, 10, 200.00, 'Paris Sculpture Lab', 'Hands-on sculpting techniques.', 'advanced');

INSERT INTO CommunityMember (name, email, birth_year, phone, city, membership_type) VALUES
('Alice Wonderland', 'alice@art.com', 1998, '666666666', 'Paris', 'premium'),
('Bob Ross', 'bob@happytrees.com', 1985, '777777777', 'London', 'free'),
('Charlie Brown', 'charlie@peanuts.com', 1995, '888888888', 'New York', 'premium');

INSERT INTO Member_Favorite_Discipline VALUES
(1,1),
(1,3),
(2,1),
(3,2);

INSERT INTO Booking (workshop_id, member_id, booking_date, payment_status) VALUES
(1,1,NOW(),'PAID'),
(2,2,NOW(),'PENDING'),
(3,3,NOW(),'PAID');

INSERT INTO Review (member_id, artwork_id, rating, comment, review_date) VALUES
(1,1,5,'Excellent work and composition.','2026-04-01'),
(2,2,4,'Very expressive use of light.','2026-04-02'),
(3,3,5,'Impressive sculpture and detail.','2026-04-03');

-- View 1: Public artist overview
CREATE VIEW vw_artist_overview AS
SELECT 
    artist_id,
    name,
    city,
    contact_email,
    birth_year,
    is_active
FROM Artist;

-- View 2: Exhibition details with gallery
CREATE VIEW vw_exhibition_details AS
SELECT 
    e.exhibition_id,
    e.title AS exhibition_title,
    e.start_date,
    e.end_date,
    e.theme,
    g.name AS gallery_name,
    g.address
FROM Exhibition e
JOIN Gallery g ON e.gallery_id = g.gallery_id;

-- View 3: Workshop bookings summary
CREATE VIEW vw_workshop_booking_summary AS
SELECT
    w.workshop_id,
    w.title,
    w.date,
    w.max_participants,
    COUNT(b.booking_id) AS current_bookings
FROM Workshop w
LEFT JOIN Booking b ON w.workshop_id = b.workshop_id
GROUP BY w.workshop_id, w.title, w.date, w.max_participants;

-- Index 1: Artist name
CREATE INDEX idx_artist_name ON Artist(name);

-- Index 2: Artwork title
CREATE INDEX idx_artwork_title ON Artwork(title);

-- Index 3: Workshop date
CREATE INDEX idx_workshop_date ON Workshop(date);

-- Trigger 1: Validate workshop capacity before booking
DELIMITER $$

CREATE TRIGGER trg_before_booking_insert
BEFORE INSERT ON Booking
FOR EACH ROW
BEGIN
    DECLARE booked_count INT;
    DECLARE max_count INT;

    SELECT COUNT(*), max_participants
    INTO booked_count, max_count
    FROM Booking b
    JOIN Workshop w ON b.workshop_id = w.workshop_id
    WHERE b.workshop_id = NEW.workshop_id
    GROUP BY w.max_participants;

    IF booked_count IS NULL THEN
        SET booked_count = 0;
        SELECT max_participants INTO max_count
        FROM Workshop
        WHERE workshop_id = NEW.workshop_id;
    END IF;

    IF booked_count >= max_count THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Workshop capacity exceeded';
    END IF;
END$$

DELIMITER ;
 
 -- Trigger 2: Validate exhibition dates
 DELIMITER $$

CREATE TRIGGER trg_before_exhibition_insert
BEFORE INSERT ON Exhibition
FOR EACH ROW
BEGIN
    IF NEW.end_date IS NOT NULL AND NEW.end_date < NEW.start_date THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Exhibition end date must be after start date';
    END IF;
END$$

DELIMITER ;

-- Trigger 3: Validate review rating
DELIMITER $$

CREATE TRIGGER trg_before_review_insert
BEFORE INSERT ON Review
FOR EACH ROW
BEGIN
    IF NEW.rating < 1 OR NEW.rating > 5 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Review rating must be between 1 and 5';
    END IF;
END$$

DELIMITER ;

-- Procedure 1: Add a new booking
DELIMITER $$

CREATE PROCEDURE sp_add_booking (
    IN p_workshop_id INT,
    IN p_member_id INT,
    IN p_payment_status VARCHAR(20)
)
BEGIN
    INSERT INTO Booking(workshop_id, member_id, booking_date, payment_status)
    VALUES (p_workshop_id, p_member_id, NOW(), p_payment_status);
END$$

DELIMITER ;

-- Procedure 2: Add a new review
DELIMITER $$

CREATE PROCEDURE sp_add_review (
    IN p_member_id INT,
    IN p_artwork_id INT,
    IN p_rating INT,
    IN p_comment TEXT
)
BEGIN
    INSERT INTO Review(member_id, artwork_id, rating, comment, review_date)
    VALUES (p_member_id, p_artwork_id, p_rating, p_comment, CURDATE());
END$$

DELIMITER ;

-- Procedure 3: Create a workshop
DELIMITER $$

CREATE PROCEDURE sp_create_workshop (
    IN p_instructor_artist_id INT,
    IN p_title VARCHAR(150),
    IN p_date DATETIME,
    IN p_duration INT,
    IN p_max_participants INT,
    IN p_price DECIMAL(10,2),
    IN p_location VARCHAR(150),
    IN p_description TEXT,
    IN p_level VARCHAR(30)
)
BEGIN
    INSERT INTO Workshop(
        instructor_artist_id, title, date, duration_minutes, max_participants,
        price, location, description, level
    )
    VALUES(
        p_instructor_artist_id, p_title, p_date, p_duration, p_max_participants,
        p_price, p_location, p_description, p_level
    );
END$$

DELIMITER ;

-- Function 1: Number of bookings in a workshop
DELIMITER $$

CREATE FUNCTION fn_booking_count(p_workshop_id INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total
    FROM Booking
    WHERE workshop_id = p_workshop_id;
    RETURN total;
END$$

DELIMITER ;

-- Function 2: Average artwork rating
DELIMITER $$

CREATE FUNCTION fn_artwork_average_rating(p_artwork_id INT)
RETURNS DECIMAL(3,2)
DETERMINISTIC
BEGIN
    DECLARE avg_rating DECIMAL(3,2);
    SELECT AVG(rating) INTO avg_rating
    FROM Review
    WHERE artwork_id = p_artwork_id;
    RETURN avg_rating;
END$$

DELIMITER ;

-- Function 3: Available seats in workshop
DELIMITER $$

CREATE FUNCTION fn_available_seats(p_workshop_id INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE max_count INT;
    DECLARE booked_count INT;

    SELECT max_participants INTO max_count
    FROM Workshop
    WHERE workshop_id = p_workshop_id;

    SELECT COUNT(*) INTO booked_count
    FROM Booking
    WHERE workshop_id = p_workshop_id;

    RETURN max_count - booked_count;
END$$

DELIMITER ;

-- transaction scenario 
START TRANSACTION;

INSERT INTO Booking(workshop_id, member_id, booking_date, payment_status)
VALUES (1, 2, NOW(), 'PAID');

INSERT INTO Booking(workshop_id, member_id, booking_date, payment_status)
VALUES (3, 2, NOW(), 'PAID');

COMMIT;

-- rollback 
START TRANSACTION;

INSERT INTO Booking(workshop_id, member_id, booking_date, payment_status)
VALUES (1, 1, NOW(), 'PAID');

-- INSERT INTO Booking(workshop_id, member_id, booking_date, payment_status)
-- VALUES (1, 1, NOW(), 'PAID');

ROLLBACK;
