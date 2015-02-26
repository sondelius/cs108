package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.Test;

import sql.SQL;

public class TagTest {

	/**
	 * Tests creating a new tag.
	 */
	@Test
	public void testCreateTag() {
		try {
			SQL.init();
			// Check number of old tags
			Statement s = SQL.getStatement();
			ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Tags;");
			rs.next();
			int oldNumberOfTags = rs.getInt(1);
			// Add a new tag
			Tag t = Tag.getOrCreateNewTag("TestTag" + System.currentTimeMillis());
			rs = s.executeQuery("SELECT COUNT(*) FROM Tags;");
			rs.next();
			assertEquals(rs.getInt(1) - oldNumberOfTags, 1);
			// Cleanup
			s.executeUpdate(String.format("DELETE FROM Tags WHERE ID = %d;",
					t.getId()));
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}

	/**
	 * Tests creating or getting a tag, including matching on fragments of a tag
	 * name.
	 */
	@Test
	public void testGetTag() {
		try {
			SQL.init();
			// Add a new tag
			String tagName = "TestTag" + System.currentTimeMillis();
			Tag t = Tag.getOrCreateNewTag(tagName);
			Statement s = SQL.getStatement();
			ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Tags;");
			rs.next();
			int oldNumberOfTags = rs.getInt(1);
			// Grab that tag, causing nothing to be created because it was created
			// already.
			t = Tag.getOrCreateNewTag(tagName);
			rs = s.executeQuery("SELECT COUNT(*) FROM Tags;");
			rs.next();
			assertEquals(rs.getInt(1) - oldNumberOfTags, 0);
			// Test grabbing that tag by name
			Tag t2 = Tag.getTag(tagName);
			assertEquals(t.getId(), t2.getId());
			// Create another tag with similar name
			// Add another tag with a similar name
			Tag t3 = Tag.getOrCreateNewTag("stTag" + System.currentTimeMillis());
			// Search for all tags containing a fragment (should get both)
			ArrayList<Tag> similarTags = Tag.getTags("stTag");
			boolean foundOldTag = false, foundNewTag = false;
			for (Tag similarTag : similarTags) {
				if (similarTag.getId() == t.getId()) {
					foundOldTag = true;
				} else if (similarTag.getId() == t3.getId()) {
					foundNewTag = true;
				}
			}
			assertTrue(foundOldTag && foundNewTag);
			// Cleanup
			s.executeUpdate(String.format("DELETE FROM Tags WHERE ID = %d;",
					t.getId()));
			s.executeUpdate(String.format("DELETE FROM Tags WHERE ID = %d;",
					t3.getId()));
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}

}
