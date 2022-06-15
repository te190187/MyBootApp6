CREATE TABLE IF NOT EXISTS books (
  id int auto_increment not null,
  title char,
  writter char,
  publisher char,
  price int,
  PRIMARY KEY(id)
);

INSERT INTO books (title, writter, publisher, price)
  VALUES ('書籍1', '東北タロウ', '出版社1', 100)