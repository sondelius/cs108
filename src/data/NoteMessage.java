package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents notes that have been issued.
 * 
 * @author Kevin
 *
 */
public class NoteMessage implements Message {
	private int id;
	private int senderId;
	private int receiverId;
	private Date sentDate;
	private String contents;
	private boolean seen;

	/**
	 * Private constructor for creating FriendMessage objects.
	 * 
	 * @param id
	 *          The note ID to identify this note by.
	 * @param senderId
	 *          The account ID of the sender.
	 * @param receiverId
	 *          The account ID of the receiver.
	 * @param sentDate
	 *          The date the message was sent on.
	 * @param contents
	 *          The text in the note.
	 * @param seen
	 *          Whether or not the note has been viewed.
	 */
	private NoteMessage(int id, int senderId, int receiverId, Date sentDate,
			String contents, boolean seen) {
		this.id = id;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.sentDate = sentDate;
		this.contents = contents;
		this.seen = seen;
	}

	/**
	 * Fetches all NoteMessages for a given account.
	 * 
	 * @param a
	 *          The account to fetch all messages for.
	 * @return An ArrayList of all notes to this account.
	 * @throws SQLException
	 *           If errors happen while querying the SQL database.
	 */
	public static ArrayList<Message> getAllMessages(Account a)
			throws SQLException {
		// TODO: Fetch all rows from the Notes table that include Account
		// a's ID as the recipient and use the NoteMessage constructor to create
		// instances of NoteMessage to populate the arraylist. For parsing Dates,
		// see SQL.convertSQLDateToDate.
		return null;
	}

	/**
	 * Issues a new note, creating an entry the SQL database to represent the
	 * note.
	 * 
	 * @param requester
	 *          The account requesting the friendship.
	 * @param receiver
	 *          The account receiving the request.
	 * @param contents
	 *          The text in the note.
	 * @return The NoteMessage representing that request.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Message issueNewNote(Account requester, Account receiver,
			String contents) throws SQLException {
		// TODO: Check if accounts are not the same, then do the following: Create
		// an appropriate entry in the SQL table and create a NoteMessage for that
		// new entry, returning it.
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

	public boolean isSeen() {
		return seen;
	}

	public String getContents() {
		return contents;
	}

	/**
	 * Views the current note.
	 * 
	 * @throws SQLException
	 *           If SQL operations fail.
	 */
	public void view() throws SQLException {
		// TODO: Modify SQL tables to set the note to be viewed. To find the note,
		// search by note ID.
	}
}
