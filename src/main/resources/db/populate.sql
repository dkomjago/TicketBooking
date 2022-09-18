INSERT INTO ROOM (ROOM_TYPE, ROOM_NAME)
VALUES
    ('PRIVATE', 'ROYAL THEATRE'),
    ('PRIVATE', 'PREMIERE ROOM'),
    ('PUBLIC', 'ROOM 1'),
    ('PUBLIC', 'ROOM 2'),
    ('PUBLIC', 'ROOM 3'),
    ('PUBLIC', 'ROOM 4');

INSERT INTO SEAT (ROOM_ID, SEAT_ROW, SEAT_NUMBER)
SELECT ROOM_ID, SEAT_ROW, SEAT_NUMBER FROM (SELECT 1 AS ROOM_ID UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6) a
CROSS JOIN
(SELECT 1 AS SEAT_NUMBER UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
    UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
    UNION SELECT 11 UNION SELECT 12) b
CROSS JOIN
(SELECT 'A' AS SEAT_ROW UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E') c
ORDER BY ROOM_ID ASC, SEAT_ROW ASC, SEAT_NUMBER ASC;

INSERT INTO TICKET_TYPE (TYPE_NAME, PRICE)
VALUES
    ('ADULT', 25),
    ('PROMO', 12.5),
    ('CHILD', 15),
    ('STUDENT', 17);

INSERT INTO MOVIE (TITLE, MOVIE_CAST, DIRECTOR, DESCRIPTION, DURATION_MINUTES)
VALUES
    ('Movie: Episode I', 'Dude', 'George Lucas', 'Best one yet', 120),
    ('Movie: Episode II', 'Dude', 'Pete Dookas', 'Worst one yet', null),
    ('Movie: Episode III', 'Dude', 'Tom Putas', null, null),
    ('Movie: Episode IV', 'Dude', null, null, null),
    ('Movie: Episode V', null, null, null, null);

INSERT INTO SCREENING (ROOM_ID, MOVIE_ID, STARTING_TIME)
VALUES
    (1, 1, CURRENT_TIMESTAMP + INTERVAL '1' DAY + INTERVAL '3' HOUR),
    (1, 1, CURRENT_TIMESTAMP + INTERVAL '1' DAY + INTERVAL '3' HOUR),
    (1, 1, CURRENT_TIMESTAMP + INTERVAL '1' DAY + INTERVAL '3' HOUR),
    (1, 1, CURRENT_TIMESTAMP + INTERVAL '2' DAY),
    (1, 1, CURRENT_TIMESTAMP + INTERVAL '3' DAY),
    (1, 2, CURRENT_TIMESTAMP + INTERVAL '3' DAY + INTERVAL '3' HOUR),
    (1, 2, CURRENT_TIMESTAMP + INTERVAL '3' DAY + INTERVAL '6' HOUR),
    (2, 3, CURRENT_TIMESTAMP + INTERVAL '1' DAY + INTERVAL '3' HOUR),
    (2, 3, CURRENT_TIMESTAMP + INTERVAL '1' DAY + INTERVAL '3' HOUR),
    (2, 3, CURRENT_TIMESTAMP + INTERVAL '1' DAY + INTERVAL '3' HOUR),
    (2, 3, CURRENT_TIMESTAMP + INTERVAL '2' DAY),
    (2, 3, CURRENT_TIMESTAMP + INTERVAL '2' DAY + INTERVAL '7' HOUR),
    (2, 3, CURRENT_TIMESTAMP + INTERVAL '3' DAY + INTERVAL '3' HOUR),
    (2, 3, CURRENT_TIMESTAMP + INTERVAL '3' DAY + INTERVAL '6' HOUR),
    (2, 4, CURRENT_TIMESTAMP + INTERVAL '4' DAY),
    (2, 4, CURRENT_TIMESTAMP + INTERVAL '4' DAY + INTERVAL '3' HOUR),
    (2, 4, CURRENT_TIMESTAMP + INTERVAL '4' DAY + INTERVAL '6' HOUR),
    (2, 4, CURRENT_TIMESTAMP + INTERVAL '4' DAY + INTERVAL '9' HOUR),
    (2, 5, CURRENT_TIMESTAMP + INTERVAL '5' DAY);

INSERT INTO SEAT_DETAILS (SEAT_ID, SEAT_TYPE, COST_MULTIPLIER, IS_AVAILABLE)
SELECT ID AS SEAT_ID, SEAT_TYPE,
        ((12 - ABS(6 - SEAT_NUMBER)) * SEAT_MULTIPLIER / 10) AS COST_MULTIPLIER,
        TRUE AS IS_AVAILABLE
 FROM
    (SELECT ID, SEAT_ROW, SEAT_NUMBER,
        CASE
            WHEN SEAT_ROW ='D' THEN 'VIP'
            WHEN SEAT_ROW ='E' THEN 'DOUBLE'
            ELSE 'REGULAR' END
            AS SEAT_TYPE,
        CASE
            WHEN SEAT_ROW ='D' THEN 1.5
            WHEN SEAT_ROW ='E' THEN 2.0
            ELSE 1.0 END
            AS SEAT_MULTIPLIER
     FROM SEAT);

INSERT INTO RESERVATION (BUYER_NAME, EXPIRATION_TIME)
VALUES
    ('Tester Testerson', CURRENT_TIMESTAMP + INTERVAL '15' MINUTE),
    ('Tester Testerson', CURRENT_TIMESTAMP + INTERVAL '15' MINUTE);

INSERT INTO TICKET (RESERVATION_ID, SCREENING_ID, SEAT_ID, TICKET_TYPE_ID)
VALUES
    (1, 1, 1, 1),
    (1, 1, 2, 1);

GO