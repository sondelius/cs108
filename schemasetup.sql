USE c_cs108_hzheng1;
-- >>>>>>>>>>>> change this line so it uses your database, not mine <<<<<<<<<<<<<<<
  
-- Drop all tables if they already exist
DROP TABLE IF EXISTS Reports;
DROP TABLE IF EXISTS Ratings;
DROP TABLE IF EXISTS QuizTaggings;
DROP TABLE IF EXISTS Tags;
DROP TABLE IF EXISTS EarnedAchievements;
DROP TABLE IF EXISTS Achievements;
DROP TABLE IF EXISTS HistoryEntries;
DROP TABLE IF EXISTS Notes;
DROP TABLE IF EXISTS PendingChallenges;
DROP TABLE IF EXISTS PendingFriendRequests;
DROP TABLE IF EXISTS Friends;
DROP TABLE IF EXISTS Questions;
DROP TABLE IF EXISTS Quizzes;
DROP TABLE IF EXISTS Accounts;
DROP TABLE IF EXISTS Categories;

-- Create fresh tables

--																NOTICE: A note about FOREIGN KEYS																--
-- Foreign keys dictate dependencies across tables. For example, if I have a table with column A
-- and another table with a column B which has FOREIGN KEY on A, it means all values in column BACKUP
-- MUST have a corresponding equivalent entry in A. What that means is you CANNOT delete an entry in
-- the table with column A if an entry in table with column B references that value. Ex: you can
-- delete a person with location Stanford at any time; you cannot delete location Stanford if there
-- exists people with location Stanford if location column foreign keys. Thus, you must delete all
-- people with location Stanford (or set the location elsewhere) before obliterating Stanford in your
-- table of locations. ORDER OF EDITS MATTER!

-- >>>> Other Entity Tables Section Start

-- Represents the set of all possible categories
CREATE TABLE Categories (
	-- The ID of the category (AUTO_INCREMENT) implies that creation
	-- or insertion don't need to specify the value.
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	-- Represents the actual string name of the category.
	CategoryName VARCHAR(255) NOT NULL UNIQUE
);
INSERT INTO Categories (CategoryName) VALUES ('None');

-- Represents all accounts on the website.
CREATE TABLE Accounts (
	-- A numerical ID for this account (NOT THE USERNAME).
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	-- The username of the account, to be unique.
	Username VARCHAR(255) NOT NULL UNIQUE,
	-- The password hash SHA1 of the account, accounting the salt we use
	-- to secure passwords.
	Password CHAR(40),
	-- Whether or not the user is an admin. Defaults to false.
	IsAdmin BOOLEAN DEFAULT FALSE
);
-- Create a single admin account (salt is 'cs108proj').
-- User cs108admin, Pass admincs108
INSERT INTO Accounts (Username, Password, IsAdmin) VALUES ('cs108admin', '8946b27a26264f322fdb283ea29f728e7f5cd702', TRUE);

-- Represents a single quiz.
CREATE TABLE Quizzes (
	-- The unique ID of the quiz.
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	-- The text name of the quiz, not necessarily unique.
	Name VARCHAR(255),
	-- The ID of the creator of the quiz. MUST HAVE A CORRESPONDING ACCOUNT
	CreatorID INT NOT NULL,
	FOREIGN KEY (CreatorID) REFERENCES Accounts(ID),
	-- The time the quiz was created
	TimeCreated DATE,
	-- The category of the quiz. MUST HAVE A CORRESPONDING CATEGORY (it can be the ID of "None").
	CategoryID INT NOT NULL,
	FOREIGN KEY (CategoryID) REFERENCES Categories(ID),
	-- Whether or not to display the questions in random order.
	RandomOrder BOOLEAN,
	-- Whether or not to singledisplay questions or multidisplay questions.
	MultiPage BOOLEAN,
	-- Whether or not to allow for immediate display of correct answers.
	ImmediateCorrection BOOLEAN,
	-- Whether or not to allow practice mode takes.
	AllowPractice BOOLEAN
);

-- Represents questions and holding quizzes. Contains data that should be parsed to generate actual question contents.
CREATE TABLE Questions (
  -- The ID of the question
  ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  -- The ID of the question that belongs in the quiz
  QuizID INT NOT NULL,
  FOREIGN KEY (QuizID) REFERENCES Quizzes(ID),
  -- The type of question. 
  QuestionType INT NOT NULL,
  -- The index of the question in the quiz.
  QuestionNumber INT NOT NULL,
  -- The data of the question, up to 4000 characters.
  Question VARCHAR(4000)
);

-- Represents friend relationships. Hold 2 entries for each friend relationship for convenience.
-- For example, if entry (A, B) exists in the table, there MUST be a corresponding (B, A) in the
-- table.
CREATE TABLE Friends (
	-- The ID of the first user in the friendship.
	UserID INT NOT NULL,
	FOREIGN KEY (UserID) REFERENCES Accounts(ID),
	-- The ID of the second user in the friendship.
	FriendID INT NOT NULL,
	FOREIGN KEY (FriendID) REFERENCES Accounts(ID)
);

-- Represents all pending friend requests. Only create one if a user requests to be a friend with
-- someone is not friends with them. If a user requests to be a friend with a user that has initiated
-- a friend request with them pending, DO NOT issue dual requests; simply remove the pending request
-- and make them friends. DO NOT issue friend requests from the same user to himself. Remove the
-- request once another user has Accepted or Declined
CREATE TABLE PendingFriendRequests (
	-- The ID of the issuer of the request.
	RequesterID INT NOT NULL,
	FOREIGN KEY (RequesterID) REFERENCES Accounts(ID),
	-- The ID of the receiver of the request.
	ReceiverID INT NOT NULL,
	FOREIGN KEY (ReceiverID) REFERENCES Accounts(ID),
	-- The date of when the request was made.
	SentTime DATE
);

-- Represents all pending quiz challenges. Remove when a user accepts or declines the challenge.
CREATE TABLE PendingChallenges (
	-- The ID of the issuer of the request.
	RequesterID INT NOT NULL,
	FOREIGN KEY (RequesterID) REFERENCES Accounts(ID),
	-- The ID of the receiver of the request.
	ReceiverID INT NOT NULL,
	FOREIGN KEY (ReceiverID) REFERENCES Accounts(ID),
	-- The date of when the request was made.
	SentTime DATE,
	-- The ID of the quiz being sent as a challenge.
	QuizID INT NOT NULL,
	FOREIGN KEY (QuizID) REFERENCES Quizzes(ID)
);

-- Represents all notes that are sent between users. Notes exist forever but can be set as "seen".
-- A "seen" notice will exist and can be reviewed but won't be annoying and appear on the newsfeed.
CREATE TABLE Notes (
	-- The ID to identify this note by
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	-- The ID of the issuer of the request.
	RequesterID INT NOT NULL,
	FOREIGN KEY (RequesterID) REFERENCES Accounts(ID),
	-- The ID of the receiver of the request.
	ReceiverID INT NOT NULL,
	FOREIGN KEY (ReceiverID) REFERENCES Accounts(ID),
	-- The date of when the request was made.
	SentTime DATE,
	-- The text contents of the actual note.
	Note VARCHAR(2000),
	-- Whether or not the note has been seen. All created notes are defaulted to not seen and thus
	-- this field does not need to be included.
	Seen BOOLEAN DEFAULT FALSE
);

-- Represents a single instance of someone taking a quiz
CREATE TABLE HistoryEntries (
	-- The ID of the user that took the test.
	UserID INT NOT NULL,
	FOREIGN KEY (UserID) REFERENCES Accounts(ID),
	-- The ID of the quiz taken.
	QuizID INT NOT NULL,
	FOREIGN KEY (QuizID) REFERENCES Quizzes(ID),
	-- The score of the test take.
	Score INTEGER,
	-- When the test was taken.
	TimeBegun DATE,
	-- How long it took to take the test.
	TimeTaken TIME
);

-- Represents all of the achievements earnable possible
CREATE TABLE Achievements (
	-- The ID of the achievement.
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	-- The name of the achievement.
	Name VARCHAR(255) NOT NULL,
	-- The URL of the icon of the achievement.
	IconSrc VARCHAR(255) NOT NULL,
	-- The tooltip about the achievement.
	Tooltip VARCHAR(1000)
);
-- TODO: create the achievements and put them in the table here

-- Represents all the achievements earned. For each user, for each achievement they earn,
-- there will be one entry in  here.
CREATE TABLE EarnedAchievements (
	-- The ID of the user that earned the achievement.
	UserID INT NOT NULL,
	FOREIGN KEY (UserID) REFERENCES Accounts(ID),
	-- The earned achievement's ID.
	AchievementID INT NOT NULL,
	FOREIGN KEY (AchievementID) REFERENCES Achievements(ID)
);

-- Represents all tags that quizzes can be tagged with.
CREATE TABLE Tags (
	-- The ID of the tag.
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	-- The text of the tag. Must be unique.
	TagName VARCHAR(255) NOT NULL UNIQUE
);

-- Represents all tags assigned to quizzes. For each quiz, for each tag that it has been assigned,
-- there will be one entry here.
CREATE TABLE QuizTaggings (
	-- The ID of the quiz.
	QuizID INT NOT NULL,
	FOREIGN KEY (QuizID) REFERENCES Quizzes(ID),
	-- The ID of the tag assigned to that quiz.
	TagID INT NOT NULL,
	FOREIGN KEY (TagID) REFERENCES Tags(ID)
);
	
-- Represents all ratings people give for quizzes.
CREATE TABLE Ratings (
	-- The ID of the quiz.
	QuizID INT NOT NULL,
	FOREIGN KEY (QuizID) REFERENCES Quizzes(ID),
	-- The ID of the user that rated the quiz.
	UserID INT NOT NULL,
	FOREIGN KEY (UserID) REFERENCES Accounts(ID),
	-- The rating grade of the rating, from a scale of 0 to 5.
	Rating INT NOT NULL,
	-- An optional nullable review that explains the rating.
	Review VARCHAR(2000)
);

-- Represents all reports people make on quizzes.
CREATE TABLE Reports (
	-- The ID of the quiz.
	QuizID INT NOT NULL,
	FOREIGN KEY (QuizID) REFERENCES Quizzes(ID),
	-- The ID of the user that rated the quiz.
	UserID INT NOT NULL,
	FOREIGN KEY (UserID) REFERENCES Accounts(ID),
	-- An optional nullable reason that explains the report.
	Reason VARCHAR(2000)
);
	
