INSERT INTO publishers (publisher_name) VALUES
('WSIP'),
('Nowa Era');

INSERT INTO authors (author_name, author_surname) VALUES
('Ernest','Hemingway'),
('Henryk','Sienkiewicz'),
('Adam','Małysz'),
('Jacek','Piekara'),
('Marcin','Drel'),
('Oscar','Wilde'),
('Anne','McCaffrey'),
('Meg','Cabot'),
('Tom','Clancy'),
('Marek','Szolc');

INSERT INTO books (book_name, isbn, publisher_id, available_quantity, image_url) VALUES
('W pustyni i w puszczy','9892038573123',1,10,'assets/images/books/book.png'),
('Ferdydurke','9392857201921',1,20, 'assets/images/books/book.png'),
('Rok 1984','9392857201921',1,20, 'assets/images/books/book.png'),
('Krótka historia czasu','9392857201921',1,20, 'assets/images/books/book.png'),
('Oddech, oczy, pamięć','9392857201921',1,20, 'assets/images/books/book.png'),
('Tłumacz chorób','9392857201921',1,20, 'assets/images/books/book.png'),
('Strzelby, zarazki, maszyny. Losy ludzkich społeczeństw','9392857201921',1,20, 'assets/images/books/book.png'),
('Charlie i fabryka czekolady','9392857201921',1,20, 'assets/images/books/book.png'),
('Biblia jadowitego drzewa','9392857201921',1,20, 'assets/images/books/book.png'),
('Słońce też wschodzi','9392857201921',1,20, 'assets/images/books/book.png'),
('Popiół i żar: wspomnienie','9392857201921',1,20, 'assets/images/books/book.png'),
('O czym szumią wierzby','9392857201921',1,20, 'assets/images/books/book.png'),
('Dawca','9392857201921',1,20, 'assets/images/books/book.png'),
('Najlepsi. Kowboje, którzy polecieli w kosmos','9392857201921',1,20, 'assets/images/books/book.png'),
('Bardzo głodna gąsienica','9392857201921',1,20, 'assets/images/books/book.png'),
('Wszyscy ludzie prezydenta','9392857201921',1,20, 'assets/images/books/book.png');

INSERT INTO librarians (librarian_name, librarian_surname) VALUES
('Marek','Kowal'),
('Jurek','Kozak');

INSERT INTO book_author (id_book, id_author) VALUES
(1,2),
(2,1),
(3,1),
(4,1),
(5,1),
(6,1),
(7,1),
(8,1),
(9,1),
(10,1),
(11,1),
(12,1),
(13,1),
(14,1),
(15,1),
(16,1);

