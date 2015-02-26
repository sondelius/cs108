package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import sql.SQL;

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
		Statement s = SQL.getStatement();
		ResultSet rs = s.executeQuery(String.format(
				"SELECT * FROM Categories WHERE CategoryName = '%s';", exactName));
		Category result = null;
		if (rs.next()) {
			result = new Category(rs.getString("CategoryName"), rs.getInt("ID"));
		}
		s.close();
		return result;
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
		Statement s = SQL.getStatement();
		ResultSet rs = s.executeQuery(String.format(
				"SELECT * FROM Categories WHERE CategoryName LIKE '%s';", "%"
						+ fragment + "%"));
		ArrayList<Category> result = new ArrayList<Category>();
		while (rs.next()) {
			result.add(new Category(rs.getString("CategoryName"), rs.getInt("ID")));
		}
		s.close();
		return result;
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
		Category c = getCategory(name);
		if (c == null) {
			// Create new category
			Statement s = SQL.getStatement();
			s.executeUpdate(String.format(
					"INSERT INTO Categories (CategoryName) VALUES ('%s');", name));
			ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID() FROM Categories;");
			rs.next();
			c = new Category(name, rs.getInt(1));
			s.close();
		}
		return c;
	}

	/**
	 * Gets a list of categories by popularity, with a limit to the number to
	 * retrieve.
	 * 
	 * @param limit
	 *          The upper limit on the number of categories to get; use -1 to
	 *          signify no limit.
	 * @return An ArrayList of categories.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static ArrayList<Category> getCategories(int limit)
			throws SQLException {
		Statement s = SQL.getStatement();
		ResultSet rs = s
				.executeQuery(String
						.format("SELECT CategoryName FROM Quizzes LEFT JOIN Categories ON Quizzes.CategoryID = Categories.ID"
								+ (limit == -1 ? "" : (" LIMIT " + limit)) + ";"));
		ArrayList<Category> result = new ArrayList<Category>();
		while (rs.next()) {
			result.add(new Category(rs.getString("CategoryName"), rs.getInt("ID")));
		}
		s.close();
		return result;
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
