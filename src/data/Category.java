package data;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Containers for data in the Categories table.
 * 
 * @author Kevin
 *
 */
public class Category {
	/**
	 * Fetches the category with the given name.
	 * 
	 * @param exactName
	 *          The category name to search by.
	 * @return The category if it exists, or null.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Category getCategory(String exactName) throws SQLException {
		// TODO: Create an SQL query to find all entries in the Category table that
		// contain the fragment and then convert those entries into Category objects
		// by invoking the private constructior.
		return null;
	}

	/**
	 * Fetches all categories whose name contains the fragment to search by.
	 * 
	 * @param fragment
	 *          The category name fragment to search by.
	 * @return An ArrayList of all categories that contain the fragment.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static ArrayList<Category> getCategories(String fragment)
			throws SQLException {
		// TODO: Create an SQL query to find all entries in the Category table that
		// contain the fragment and then convert those entries into Category objects
		// by invoking the private constructior.
		return null;
	}

	/**
	 * Fetches a category with a given name, or if inexistent, creates a new
	 * category with that name.
	 * 
	 * @param name
	 *          The name of the category desired.
	 * @return The Category object representing the desired category.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Category getOrCreateNewCategory(String name)
			throws SQLException {
		// TODO: Check for existence; if it exists in Categories table, convert the
		// entry into a new Category object using the private constructor and return
		// it. Otherwise, insert a new entry for the newly desired category and
		// construct a Category object to represent that.
		return null;
	}

	private String name;
	private int id;

	private Category(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
}
