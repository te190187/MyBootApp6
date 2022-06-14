CREATE TABLE IF NOT EXISTS books (
  id int auto_increment not null,
  title char,
  writter char,
  publisher char,
  price int,
  PRIMARY KEY(id)
);