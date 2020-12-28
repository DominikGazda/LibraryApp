INSERT INTO publishers (publisher_name) VALUES
('WSIP'),
('Nowa Era');

INSERT INTO authors (author_name, author_surname) VALUES
('Ernest','Hemingway'),
('Henryk','Sienkiewicz'),
('Adam','Małysz');

INSERT INTO books (book_name, isbn, publisher_id, available_quantity) VALUES
('W pustyni i w puszczy','98920385731',1,10),
('Ferdydurke','93928572019',1,20);

INSERT INTO librarians (librarian_name, librarian_surname) VALUES
('Marek','Kowal'),
('Jurek','Kozak');

INSERT INTO book_author (id_book, id_author) VALUES
(1,2),
(2,1);