package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import sql.SQL;

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
		Statement s = SQL.getStatement();
		ResultSet rs = s.executeQuery(String.format(
				"SELECT * FROM Tags WHERE TagName = '%s';", exactName));
		Tag result = null;
		if (rs.next()) {
			result = new Tag(rs.getString("TagName"), rs.getInt("ID"));
		}
		s.close();
		return result;
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
		Statement s = SQL.getStatement();
		ResultSet rs = s.executeQuery(String.format(
				"SELECT * FROM Tags WHERE TagName LIKE '%s';", "%" + fragment + "%"));
		ArrayList<Tag> result = new ArrayList<Tag>();
		while (rs.next()) {
			result.add(new Tag(rs.getString("TagName"), rs.getInt("ID")));
		}
		s.close();
		return result;
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
		Tag t = getTag(name);
		if (t == null) {
			// Create new tag
			Statement s = SQL.getStatement();
			s.executeUpdate(String.format(
					"INSERT INTO Tags (TagName) VALUES ('%s');", name));
			ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID() FROM Tags;");
			rs.next();
			t = new Tag(name, rs.getInt(1));
			s.close();
		}
		return t;
	}

	/**
	 * Gets a list of tags by popularity, with a limit to the number to retrieve.
	 * 
	 * @param limit
	 *          The upper limit on the number of tags to get; use -1 to signify no
	 *          limit.
	 * @return An ArrayList of tags.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static ArrayList<Tag> getTags(int limit) throws SQLException {
		Statement s = SQL.getStatement();
		ResultSet rs = s
				.executeQuery(String
						.format("SELECT TagName FROM QuizTaggings LEFT JOIN Tags ON QuizTaggings.TagID = Tags.ID"
								+ (limit == -1 ? "" : (" LIMIT " + limit)) + ";"));
		ArrayList<Tag> result = new ArrayList<Tag>();
		while (rs.next()) {
			result.add(new Tag(rs.getString("TagName"), rs.getInt("ID")));
		}
		s.close();
		return result;
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
