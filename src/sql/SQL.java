package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class to create Statements and Updates to interface with SQL tables.
 * All other functionalities needing to invoke MySQL commands should go through
 * this class.
 * 
 * @author Kevin
 *
 */
public class SQL {
	// Holds a persistent connection to the SQL server.
	private static Connection conn;

	/**
	 * Initializes the SQL connection. Should be called at the beginning of the
	 * lifetime of the app.
	 * 
	 * @throws SQLException
	 *           If errors occur opening a connection to the SQL server.
	 */
	public static void init() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Connect to the server and specify which database to use
		conn = DriverManager.getConnection("jdbc:mysql://"
				+ MyDBInfo.MYSQL_DATABASE_SERVER, MyDBInfo.MYSQL_USERNAME,
				MyDBInfo.MYSQL_PASSWORD);
		Statement stmt = conn.createStatement();
		stmt.executeQuery("USE " + MyDBInfo.MYSQL_DATABASE_NAME);
	}

	/**
	 * Closes the SQL connection. Should be called at the end of the lifetime of
	 * the app.
	 * 
	 * @throws SQLException
	 *           If errors occur closing a connection to the SQL server.
	 */
	public static void cleanup() throws SQLException {
		conn.close();
	}

	/**
	 * Prepare a Statement object to invoke SQL statements on.
	 * 
	 * @return A Statement object that is readily connected to the SQL server.
	 * @throws SQLException
	 *           If errors occur creating a Statement object.
	 */
	public static Statement getStatement() throws SQLException {
		// If the connection is not valid, re-initiate the connection.
		if (!conn.isValid(10)) {
			cleanup();
			init();
		}
		return conn.createStatement();
	}

	/**
	 * Converts a Java Date into a string representation of an MySQL DATETIME.
	 * 
	 * @param d
	 *          The date to convert.
	 * @return The string representation of a MySQL DATETIME.
	 */
	public static String convertDateToSQLDate(Date d) {
		return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d) + "'";
	}

	/**
	 * Converts a MySQL DATETIME string to a Java Date.
	 * 
	 * @param sqlDate
	 *          The string representation of a MySQL DATETIME.
	 * @return A Java Date representation of that date.
	 */
	public static Date convertSQLDateToDate(String sqlDate) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sqlDate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Converts a Java Date into a string representation of an MySQL TIME.
	 * 
	 * @param d
	 *          The date (time elapsed) to convert.
	 * @return The string representation of a MySQL TIME.
	 */
	public static Date convertSQLTimeToDate(String sqlTime) {
		try {
			return new SimpleDateFormat("HH:mm:ss").parse(sqlTime);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Converts a Java Date into a string representation of an MySQL TIME.
	 * 
	 * @param d
	 *          The date to convert.
	 * @return The string representation of a MySQL TIME.
	 */
	public static String convertDateToSQLTime(Date d) {
		return "'" + new SimpleDateFormat("HH:mm:ss").format(d) + "'";
	}
}
