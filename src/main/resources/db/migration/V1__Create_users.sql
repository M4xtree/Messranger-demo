CREATE TABLE users (
  id VARCHAR PRIMARY KEY,
  nickname VARCHAR NOT NULL,
  phone_number VARCHAR NOT NULL UNIQUE
);

CREATE TABLE messages(
	id VARCHAR PRIMARY KEY,
	chat_id VARCHAR,
	message_text VARCHAR,
	date DATE,
	time TIME,
	is_pinned BOOL
);
CREATE TABLE chat (
  id VARCHAR PRIMARY KEY,
  creator_id VARCHAR REFERENCES users(id),
  parent_id VARCHAR,
  pin_id VARCHAR,
  chat VARCHAR not null,
  name VARCHAR not null,
  chat_type CHAR NOT NULL
);

CREATE TABLE members(
	user_id VARCHAR REFERENCES users(id),
	chat_id VARCHAR REFERENCES chat(id),
	role VARCHAR,
	caret VARCHAR,
    PRIMARY KEY (user_id,chat_id)
);


