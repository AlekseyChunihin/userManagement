CREATE TABLE IF NOT EXISTS user_table (
  id IDENTITY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  country VARCHAR(50) NOT NULL
);

INSERT INTO USER_TABLE (name, email, country) VALUES ('name1', 'email1', 'country1');
INSERT INTO USER_TABLE (name, email, country) VALUES ('name2', 'email2', 'country2');