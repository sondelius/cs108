package data;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.Test;

import sql.SQL;

public class CategoryTest {

	/**
	 * Tests creating a new category.
	 */
	@Test
	public void testCreateCategory() {
		try {
			SQL.init();
			// Check number of old tags
			Statement s = SQL.getStatement();
			ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Categories;");
			rs.next();
			int oldNumberOfCategories = rs.getInt(1);
			// Add a new category
			Category c = Category.getOrCreateNewCategory("TestCat"
					+ System.currentTimeMillis());
			rs = s.executeQuery("SELECT COUNT(*) FROM Categories;");
			rs.next();
			assertEquals(rs.getInt(1) - oldNumberOfCategories, 1);
			// Cleanup
			s.executeUpdate(String.format("DELETE FROM Categories WHERE ID = %d;",
					c.getId()));
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}

	/**
	 * Tests creating or getting a category, including matching on fragments of a
	 * category name.
	 */
	@Test
	public void testGetCategory() {
		try {
			SQL.init();
			// Add a new category
			String categoryName = "TestCat" + System.currentTimeMillis();
			Category c1 = Category.getOrCreateNewCategory(categoryName);
			Statement s = SQL.getStatement();
			ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Categories;");
			rs.next();
			int oldNumberOfCategories = rs.getInt(1);
			// Grab that category, causing nothing to be created because it was
			// created
			// already.
			c1 = Category.getOrCreateNewCategory(categoryName);
			rs = s.executeQuery("SELECT COUNT(*) FROM Categories;");
			rs.next();
			assertEquals(rs.getInt(1) - oldNumberOfCategories, 0);
			// Test grabbing that category by name
			Category c2 = Category.getCategory(categoryName);
			assertEquals(c1.getId(), c2.getId());
			// Create another category with similar name
			Category c3 = Category.getOrCreateNewCategory("stCat"
					+ System.currentTimeMillis());
			// Search for all tags containing a fragment (should get both)
			ArrayList<Category> similarCategories = Category.getCategories("stCat");
			boolean foundOldCategory = false, foundNewCategory = false;
			for (Category similarCategory : similarCategories) {
				if (similarCategory.getId() == c1.getId()) {
					foundOldCategory = true;
				} else if (similarCategory.getId() == c3.getId()) {
					foundNewCategory = true;
				}
			}
			assertTrue(foundOldCategory && foundNewCategory);
			// Cleanup
			s.executeUpdate(String.format("DELETE FROM Categories WHERE ID = %d;",
					c1.getId()));
			s.executeUpdate(String.format("DELETE FROM Categories WHERE ID = %d;",
					c3.getId()));
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}
}
