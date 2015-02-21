package data;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Containers for data in the Tags table.
 * 
 * @author Kevin
 *
 */
public class Tag {
	/**
	 * Fetches the tag with the given name.
	 * 
	 * @param exactName
	 *          The tag name to search by.
	 * @return The tag if it exists, or null.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Tag getTag(String exactName) throws SQLException {
		// TODO: Create an SQL query to find all entries in the Tag table that
		// contain the fragment and then convert those entries into Tag objects
		// by invoking the private constructior.
		return null;
	}

	/**
	 * Fetches all tags whose name contains the fragment to search by.
	 * 
	 * @param fragment
	 *          The tag name fragment to search by.
	 * @return An ArrayList of all tags that contain the fragment.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static ArrayList<Tag> getTags(String fragment) throws SQLException {
		// TODO: Create an SQL query to find all entries in the Tags table that
		// contain the fragment and then convert those entries into Tag objects
		// by invoking the private constructior.
		return null;
	}

	/**
	 * Fetches a tag with a given name, or if inexistent, creates a new tag with
	 * that name.
	 * 
	 * @param name
	 *          The name of the tag desired.
	 * @return The Tag object representing the desired tag.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Tag getOrCreateNewTag(String name) throws SQLException {
		// TODO: Check for existence; if it exists in Tags table, convert the
		// entry into a new Tag object using the private constructor and return
		// it. Otherwise, insert a new entry for the newly desired tag and
		// construct a Tag object to represent that.
		return null;
	}

	private String name;
	private int id;

	private Tag(String name, int id) {
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
