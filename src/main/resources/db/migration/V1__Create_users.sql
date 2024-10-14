CREATE TABLE users (
  id VARCHAR PRIMARY KEY,
  nickname VARCHAR NOT NULL,
  phone_number VARCHAR NOT NULL UNIQUE
);

CREATE TABLE chat (
    id VARCHAR PRIMARY KEY,
    type VARCHAR NOT NULL,
    created_by VARCHAR NOT NULL REFERENCES users(id),
    name VARCHAR,
    description VARCHAR,
    is_private BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE messages (
    id VARCHAR PRIMARY KEY,
    chat_id VARCHAR NOT NULL REFERENCES chat(id),
    sender_id VARCHAR NOT NULL REFERENCES users(id),
    content VARCHAR NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    is_read BOOLEAN DEFAULT FALSE,
    edited_at TIMESTAMP
);


CREATE TABLE members (
    chat_id VARCHAR NOT NULL REFERENCES chat(id),
    user_id VARCHAR NOT NULL REFERENCES users(id),
    role VARCHAR DEFAULT 'member',
    can_delete_messages BOOLEAN DEFAULT FALSE,
    can_add_participants BOOLEAN DEFAULT FALSE,
    can_edit_messages BOOLEAN DEFAULT FALSE,
    caret VARCHAR,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (chat_id, user_id)
);

