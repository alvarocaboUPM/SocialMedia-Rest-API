-- Archivo que permite generar la BD para la práctica de SOS
CREATE TABLE messages (
    message_id SERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    message TEXT,
    time TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);

CREATE TABLE users (
  user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  age INT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE friends (
  user_id BIGINT NOT NULL,
  friend_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, friend_id),
  FOREIGN KEY (user_id) REFERENCES users (user_id),
  FOREIGN KEY (friend_id) REFERENCES users (user_id)
);


INSERT INTO users (name, email, age) VALUES
  ('John Doe', 'john@example.com', 28),
  ('Jane Doe', 'jane@example.com', 25),
  ('Bob Smith', 'bob@example.com', 32),
  ('Alice Johnson', 'alice@example.com', 30);

INSERT INTO messages (author_id, receiver_id, message, time)
VALUES
    (1, 2, 'Hello, how are you?', '2022-01-01 10:00:00'),
    (2, 1, 'I am good, thanks for asking!', '2022-01-01 10:01:00'),
    (1, 2, 'Want to grab lunch?', '2022-01-02 12:00:00'),
    (2, 1, 'Sure, where do you want to go?', '2022-01-02 12:05:00');
