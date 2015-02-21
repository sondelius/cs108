package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents pending challenges to take a quiz.
 * 
 * @author Kevin
 *
 */
public class ChallengeMessage implements Message {
	private int senderId;
	private int receiverId;
	private Date sentDate;
	private int quizId;

	/**
	 * Private constructor for creating FriendMessage objects.
	 * 
	 * @param senderId
	 *          The account ID of the sender.
	 * @param receiverId
	 *          The account ID of the receiver.
	 * @param sentDate
	 *          The date the message was sent on.
	 * @param quizId
	 *          The quiz ID of the quiz being sent in the challenge.
	 */
	private ChallengeMessage(int senderId, int receiverId, Date sentDate,
			int quizId) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.sentDate = sentDate;
		this.quizId = quizId;
	}

	/**
	 * Fetches all ChallengeMessages for a given account.
	 * 
	 * @param a
	 *          The account to fetch all messages for.
	 * @return An ArrayList of all challenge requests to this account.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static ArrayList<Message> getAllMessages(Account a)
			throws SQLException {
		// TODO: Fetch all rows from the FriendRequest table that include Account
		// a's ID as the recipient and use the FriendMessage constructor to create
		// instances of FriendMessage to populate the arraylist. For parsing Dates,
		// see SQL.convertSQLDateToDate.
		return null;
	}

	/**
	 * Issues a new challenge request, creating an entry the SQL database to
	 * represent the pending request.
	 * 
	 * @param requester
	 *          The account requesting the challenge.
	 * @param receiver
	 *          The account receiving the request.
	 * @return The ChallengeMessage representing that request.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Message issueNewChallengeRequest(Account requester,
			Account receiver, Quiz q) throws SQLException {
		// TODO: Check if accounts are not the same, then do the following: If a
		// pending request already exists, do nothing and just parse the entry in
		// the table as a ChallengeMessage. Otherwise, add a new entry into the
		// table and parse it as a ChallengeMessage to return.
		return null;
	}

	public Account getSender() throws SQLException {
		// TODO: Use Account.getAccountById to get the sender account.
		return null;
	}

	public Account getReceiver() throws SQLException {
		// TODO: Use Account.getAccountById to get the receiver account.
		return null;
	}

	public Date getSentDate() {
		return sentDate;
	}

	/**
	 * Gets the quiz contained in this request.
	 * 
	 * @return The quiz contained in this challenge.
	 */
	public Quiz getQuiz() {
		// TODO: call Quiz.getQuizById on the quizId
		return null;
	}

	/**
	 * Accepts the current challenge request.
	 * 
	 * @throws SQLException
	 *           If SQL operations fail.
	 */
	public Quiz accept() throws SQLException {
		// TODO: Modify SQL tables to remove the challenge request, then return the
		// result of getQuiz().
		return null;
	}

	/**
	 * Declines the current challenge request.
	 * 
	 * @throws SQLException
	 *           If SQL operations fail.
	 */
	public void decline() throws SQLException {
		// TODO: Modify SQL tables to remove the challenge request.
	}

}
