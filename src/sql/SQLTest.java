package sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;

public class SQLTest {

	/**
	 * Tests if basic operations to create, query, and drop a database work.
	 */
	@Test
	public void testDatabaseAccess() {
		try {
			SQL.init();
			Statement s = SQL.getStatement();
			s.executeUpdate("DROP TABLE IF EXISTS TestTable;");
			s.executeUpdate("CREATE TABLE TestTable (Message VARCHAR(100));");
			s.executeUpdate("INSERT INTO TestTable VALUES (\"Hello World!\");");
			s.executeUpdate("INSERT INTO TestTable VALUES (\"Hi there.\");");
			ResultSet rs = s.executeQuery("SELECT Message FROM TestTable;");
			rs.next();
			String message1 = rs.getString("Message");
			rs.next();
			String message2 = rs.getString("Message");
			assertEquals(message2, message1.equals("Hello World!") ? "Hi there."
					: "Hello World!");
			s.executeUpdate("DROP TABLE IF EXISTS TestTable;");
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}
}
