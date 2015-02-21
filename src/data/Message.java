package data;

import java.sql.SQLException;
import java.util.Date;

/**
 * Represents asynchronously received messages sent from one user to another.
 * 
 * @author Kevin
 *
 */
public interface Message {
	/**
	 * Dynamically fetches the Account of the sender using Account.getAccountById.
	 * 
	 * @return The Account of the sender.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public Account getSender() throws SQLException;

	/**
	 * Dynamically fetches the Account of the receiver using
	 * Account.getAccountById.
	 * 
	 * @return The Account of the receiver.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public Account getReceiver() throws SQLException;

	/**
	 * Gets the date the message was sent on. This field is not dynamic, so it can
	 * be locally stored as an instance variable.
	 * 
	 * @return The date the message was sent on.
	 */
	public Date getSentDate();
}
