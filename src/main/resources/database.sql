-- Table: users
CREATE TABLE users (
  id       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  blocked  BOOLEAN      NOT NULL
)
  ENGINE = InnoDB;

-- Table: roles
CREATE TABLE roles (
  id   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL
)
  ENGINE = InnoDB;

-- Table for mapping user and roles: user_roles
CREATE TABLE user_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (role_id) REFERENCES roles (id),

  UNIQUE (user_id, role_id)
)
  ENGINE = InnoDB;

-- Table: links
CREATE TABLE links (
  id   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  link VARCHAR(255) NOT NULL,
  image VARCHAR(100),
  text VARCHAR(100)
)DEFAULT CHARACTER SET utf8
  ENGINE = InnoDB;


-- Insert data

INSERT INTO links VALUES (1,'http://belchip.by/product/?selected_product=14576',
                          'http://belchip.by/sitepics/14877b.jpg','Субмодуль для Arduino 433MHz / при.');

INSERT INTO users VALUES (1, 'apanasik', '$2a$11$uSXS6rLJ91WjgOHhEGDx..VGs7MkKZV68Lv5r1uwFu7HgtRn3dcXG', FALSE);

INSERT INTO roles VALUES (1, 'ROLE_USER');
INSERT INTO roles VALUES (2, 'ROLE_ADMIN');

INSERT INTO user_roles VALUES (1, 2);