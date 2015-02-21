package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents pending friend requests.
 * 
 * @author Kevin
 *
 */
public class FriendMessage implements Message {
	private int senderId;
	private int receiverId;
	private Date sentDate;

	/**
	 * Private constructor for creating FriendMessage objects.
	 * 
	 * @param senderId
	 *          The account ID of the sender.
	 * @param receiverId
	 *          The account ID of the receiver.
	 * @param sentDate
	 *          The date the message was sent on.
	 */
	private FriendMessage(int senderId, int receiverId, Date sentDate) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.sentDate = sentDate;
	}

	/**
	 * Fetches all FriendMessages for a given account.
	 * 
	 * @param a
	 *          The account to fetch all messages for.
	 * @return An ArrayList of all friend requests to this account.
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
	 * Issues a new friend request, creating an entry the SQL database to
	 * represent the pending request.
	 * 
	 * @param requester
	 *          The account requesting the friendship.
	 * @param receiver
	 *          The account receiving the request.
	 * @return The FriendMessage representing that request.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Message issueNewFriendRequest(Account requester,
			Account receiver) throws SQLException {
		// TODO: Check if accounts are not the same, then do the following: If a
		// pending friend request already exists between the two users, do nothing
		// and create a new FriendMessage based on the existing data to return. If a
		// pending friend request already exists but in the inverse order, we assume
		// that both users consent to friendship simply create a new FriendMessage
		// based on the inverse entry and call accept on it. Finally, if no entry
		// exists, create an appropriate entry in the SQL table and create a
		// FriendMessage for that new entry, returning it.
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
	 * Accepts the current friend request, creating the friendship.
	 * 
	 * @throws SQLException
	 *           If SQL operations fail.
	 */
	public void accept() throws SQLException {
		// TODO: Modify SQL tables to accept a friend request and remove this
		// request from the FriendRequest table. Never forget that friendship
		// entries should have dual entries: When adding A friends with B to the
		// friends table, ALWAYS add B friends with A as well.
	}

	/**
	 * Rejects the current friend request, removing the request.
	 * 
	 * @throws SQLException
	 *           If SQL operations fail.
	 */
	public void decline() throws SQLException {
		// TODO: Modify SQL tables to remove this request from the FriendRequest
		// table.
	}

}
