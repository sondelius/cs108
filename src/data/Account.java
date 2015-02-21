package data;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Represents an account on the quiz website.
 * 
 * @author Kevin
 *
 */
public class Account {
	// The salt to append to any password that is hashed.
	private static final String PASSWORD_SALT = "cs108proj";

	/**
	 * Attempts to validate an account with the given username and password.
	 * 
	 * @param username
	 *          The username of the account.
	 * @param password
	 *          The password, unsalted and unhashed, of the account.
	 * @return The account if it exists or null if the credentials did not
	 *         authenticate.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Account login(String username, String password)
			throws SQLException {
		// TODO: Query the SQL database for any entry with the given username and
		// the password, salted and SHA-1 hashed. If there is an entry, construct a
		// new instance of the Account class and populate it with the appropriate
		// user id, returning that new instance.
		return null;
	}

	/**
	 * Loads an Account with a given ID.
	 * 
	 * @param id
	 *          The ID of the account to look up.
	 * @return The account if it exists or null if no account exists with that
	 *         given ID.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Account getAccountById(int id) throws SQLException {
		// TODO: Query the SQL database for any entry with a given user ID. If there
		// is an entry, construct a new instance of the Account class and populate
		// it with the appropriate user id and username, returning that new
		// instance.
		return null;
	}

	/**
	 * Loads Accounts for account holders containing the specified username
	 * fragment in their usernames.
	 * 
	 * @param usernameFragment
	 *          The fragment of a username to search by.
	 * @return An ArrayList of results.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static ArrayList<Account> findAccountByUsername(String usernameFragment)
			throws SQLException {
		// TODO: Construct an SQL query that searches for all usernames containing
		// the fragment. For each of these rows found, construct a new instance of
		// the Account class and populate the results arraylist.
		return null;
	}

	/**
	 * Attempts to create an account with a given username and password.
	 * 
	 * @return The account if successfully created or null if there is an issue
	 *         with the proposed username (either by collision or
	 *         innapropriateness).
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Account createAccount(String username, String password)
			throws SQLException {
		// TODO: Query the SQL database for any entry with the given username. If
		// free, create a new entry with the given username and the password after
		// salting and hashing. To determine the next free ID, simply query the SQL
		// database for the maximum account ID and increment by 1 to get the next
		// free ID. If another account has the same username, return null.
		return null;
	}

	// The ID of the account
	private int id;
	// The username of the account
	private String username;

	/**
	 * Private constructor to initialize the ID of the account.
	 * 
	 * @param id
	 *          the ID of the account.
	 * @param username
	 *          the username of the account.
	 */
	private Account(int id, String username) {
		this.id = id;
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	// TODO: Each of the methods below fetches information about accounts that is
	// dynamically changing and should be fetched upon request from the SQL table.
	// Write Javadocs and implementations that load data or constructs objects
	// that load data fresh from the SQL database. When querying for a given user,
	// always place equality constraints on the id (avoid querying with clauses
	// like "WHERE username = abc" and use clauses like "WHERE id = 123" instead).
	// For results returning arraylists, return empty arraylists instead of null
	// if the tables give no values.

	public ArrayList<Account> getFriends() throws SQLException {
		// TODO: fetch the IDs of friends, then use getUserById to load those
		// Accounts
		return null;
	}

	public boolean isAdmin() throws SQLException {
		// TODO: fetch whether the user is an admin or not
		return false;
	}

	public ArrayList<Quiz> getRecentlyCreatedQuizzes() throws SQLException {
		// TODO: search for Quiz objects using Quiz.findQuizzes and providing an
		// exact username and a createdAfter date.
		return null;
	}

	public ArrayList<Achievement> getAchievements() throws SQLException {
		// TODO: fetch all achievements and use Achievement.getAchievementById to
		// populate the resulting arraylist.
		return null;
	}

	public ArrayList<Quiz> getAllCreatedQuizzes() throws SQLException {
		// TODO: search for Quiz objects using Quiz.findQuizzes and providing an
		// exact username.
		return null;
	}

	public ArrayList<History> getHistory() throws SQLException {
		// TODO: fetch all history of this account and parse the entries of the
		// History table, constructing new History objects and populating a result
		// arraylist. See SQL.convertSQLDateToDate on parsing SQL Date objects.
		return null;
	}

	public ArrayList<Message> getMessages() throws SQLException {
		// TODO: create a new ArrayList of messages, then add all entries from
		// calling FriendMessage.getAllMessages, ChallengeMessage.getAllMessages and
		// NoteMessage.getAllMessages into the result, returning it.
		return null;
	}

}
