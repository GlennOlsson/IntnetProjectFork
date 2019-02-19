CREATE TABLE Chatrooms
(
  id INT NOT NULL,
  Name INT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE Accounts
(
  username INT NOT NULL,
  date_created INT NOT NULL,
  profile_pic INT NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE Messages
(
  message INT NOT NULL,
  date INT NOT NULL,
  chatid INT NOT NULL,
  user INT NOT NULL,
  FOREIGN KEY (chatid) REFERENCES Chatrooms(id),
  FOREIGN KEY (user) REFERENCES Accounts(username)
);

CREATE TABLE Friends
(
  user1 INT NOT NULL,
  user2 INT NOT NULL,
  FOREIGN KEY (user1) REFERENCES Accounts(username),
  FOREIGN KEY (user2) REFERENCES Accounts(username)
);

CREATE TABLE Profile
(
  date_created INT NOT NULL,
  profile_pic INT NOT NULL,
  bio INT NOT NULL,
  user INT NOT NULL,
  FOREIGN KEY (user) REFERENCES Accounts(username)
);