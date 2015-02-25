package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import sql.SQL;

import com.mysql.jdbc.StringUtils;

import data.Admin.SiteStatistics;

public class AdminTest {

	/**
	 * Stub account class to test functionalities of AdminTest before Account was
	 * implemented.
	 * 
	 * @author Kevin
	 *
	 */
	private static class StubAccount extends Account {
		protected StubAccount(int id, String username) {
			super(id, username);
		}

		public static Account getStub(String username) throws SQLException {
			Statement s = SQL.getStatement();
			ResultSet rs = s
					.executeQuery("SELECT ID FROM Accounts WHERE Username = '" + username
							+ "';");
			rs.next();
			return new StubAccount(rs.getInt("ID"), username);
		}

		public ArrayList<Quiz> getAllCreatedQuizzes() {
			return new ArrayList<Quiz>();
		}
	}

	/**
	 * Tests fetching aggregate statistics on the two required statistics: users
	 * and quizzes taken.
	 */
	@Test
	public void testStatistics() {
		try {
			SQL.init();
			SiteStatistics stats = Admin.getSiteStatistics();
			// Fetch statistics manually
			Statement s = SQL.getStatement();
			ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Accounts;");
			rs.next();
			assertEquals(rs.getInt(1), stats.getNumberOfUsers());
			rs = s.executeQuery("SELECT COUNT(*) FROM HistoryEntries;");
			rs.next();
			assertEquals(rs.getInt(1), stats.getQuizzesTaken());
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}

	/**
	 * Tests pulling and adding new announcements.
	 */
	@Test
	public void testAnnouncement() {
		try {
			SQL.init();
			ArrayList<String> recentAnnouncements = Admin.getRecentAnnouncements();
			// Generate a new message that is not already in recent announcements
			String newAnnouncement = "Test Message 'asdf' : "
					+ System.currentTimeMillis();
			while (recentAnnouncements.contains(newAnnouncement)) {
				newAnnouncement += System.currentTimeMillis();
			}
			// Add it and verify it was added
			Admin.createAnnouncement(newAnnouncement);
			recentAnnouncements = Admin.getRecentAnnouncements();
			assertTrue(recentAnnouncements.contains(newAnnouncement));
			// Cleanup
			Statement s = SQL.getStatement();
			s.executeUpdate("DELETE FROM Announcements WHERE Message = '"
					+ StringUtils.escapeQuote(newAnnouncement, "'") + "';");
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}

	/**
	 * Tests promoting a dummy account to admin.
	 */
	@Test
	public void testPromoteAccount() {
		try {
			SQL.init();
			// Create new account
			Statement s = SQL.getStatement();
			String testAccountName = "testaccount" + System.currentTimeMillis();
			s.executeUpdate("INSERT INTO Accounts (Username, Password, IsAdmin) "
					+ "VALUES ('" + testAccountName
					+ "', '8946b27a26264f322fdb283ea29f728e7f5cd702', FALSE);");
			Account a = StubAccount.getStub(testAccountName);
			// Promote account and check for promotion
			Admin.promoteAccount(a);
			ResultSet rs = s
					.executeQuery(String
							.format(
									"SELECT 1 FROM Accounts WHERE ID = %d AND Username = '%s' AND IsAdmin = TRUE;",
									a.getId(), a.getUsername()));
			assertTrue(rs.next() && rs.getInt(1) == 1);
			// Delete account
			s.executeUpdate(String.format("DELETE FROM Accounts WHERE ID = %d;",
					a.getId()));
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}

	@Test
	public void testClearQuizHistory() {
		try {
			SQL.init();
			Statement s = SQL.getStatement();
			Account a = StubAccount.getStub("cs108admin");
			// Create a new quiz
			String uniqueTestName = "Test Quiz" + System.currentTimeMillis();
			String query = "INSERT INTO Quizzes (Name,CreatorID,TimeCreated,CategoryID) SELECT '"
					+ uniqueTestName
					+ "', "
					+ a.getId()
					+ ", "
					+ SQL.convertDateToSQLDate(new Date())
					+ ", MAX(Categories.ID) FROM Accounts CROSS JOIN Categories;";
			s.executeUpdate(query);
			ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID() FROM Quizzes;");
			rs.next();
			int newlyCreatedId = rs.getInt(1);
			// Add some fake history entries
			for (int i = 0; i < 5; i++) {
				s.executeUpdate(String.format(
						"INSERT INTO HistoryEntries VALUES(%d,%d,%d,%s,%s);", a.getId(),
						newlyCreatedId, 100, SQL.convertDateToSQLDate(new Date()),
						SQL.convertDateToSQLTime(new Date(1000))));
			}
			rs = s.executeQuery("SELECT COUNT(*) FROM HistoryEntries WHERE QuizID = "
					+ newlyCreatedId + ";");
			assertTrue(rs.next() && rs.getInt(1) == 5);
			// Delete history and check
			Quiz q = Quiz.getQuizById(newlyCreatedId);
			Admin.removeQuizHistory(q);
			rs = s.executeQuery("SELECT COUNT(*) FROM HistoryEntries WHERE QuizID = "
					+ newlyCreatedId + ";");
			assertTrue(rs.next() && rs.getInt(1) == 0);
			// Delete quiz
			Admin.removeQuiz(q);
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}
}
