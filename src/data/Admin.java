package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.mysql.jdbc.StringUtils;

import sql.SQL;

/**
 * Represents static functions that an admin would carry out.
 * 
 * @author Kevin
 *
 */
public class Admin {
	/**
	 * Represents site statistics.
	 * 
	 * @author Kevin
	 *
	 */
	public static class SiteStatistics {
		int numberOfUsers;
		int quizzesTaken;
		int numberOfQuizzes;

		public SiteStatistics() throws SQLException {
			Statement s = SQL.getStatement();
			ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Accounts;");
			rs.next();
			numberOfUsers = rs.getInt(1);
			rs = s.executeQuery("SELECT COUNT(*) FROM Quizzes;");
			rs.next();
			numberOfQuizzes = rs.getInt(1);
			rs = s.executeQuery("SELECT COUNT(*) FROM HistoryEntries;");
			rs.next();
			numberOfQuizzes = rs.getInt(1);
			s.close();
		}

		/**
		 * Gets total number of accounts created on the site.
		 * 
		 * @return The number of accounts.
		 */
		public int getNumberOfUsers() {
			return numberOfUsers;
		}

		/**
		 * Gets total number of attempts to take quizzes on the site.
		 * 
		 * @return The number of attempts.
		 */
		public int getQuizzesTaken() {
			return quizzesTaken;
		}

		/**
		 * Gets total number of quizzes.
		 * 
		 * @return The number of quizzes.
		 */
		public int getNumberOfQuizzes() {
			return numberOfQuizzes;
		}
	}

	/**
	 * Creates a global announcement. Announcements expire in 7 days.
	 * 
	 * @param announcement
	 *          The message of the announcement.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static void createAnnouncement(String announcement)
			throws SQLException {
		announcement = StringUtils.escapeQuote(announcement, "'");
		Statement s = SQL.getStatement();
		s.executeUpdate(String.format(
				"INSERT INTO Announcements VALUES ('%s', %s)", announcement,
				SQL.convertDateToSQLDate(new Date())));
		s.close();
	}

	/**
	 * Gets the announcements that have not expired yet.
	 * 
	 * @return An ArrayList of the announcements' messages.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static ArrayList<String> getRecentAnnouncements() throws SQLException {
		ArrayList<String> results = new ArrayList<String>();
		Statement s = SQL.getStatement();
		ResultSet rs = s.executeQuery(String.format(
				"SELECT Message FROM Announcements WHERE WrittenOn > %s;",
				SQL.convertDateToSQLDate(getMessageTimeBound())));
		while (rs.next()) {
			results.add(rs.getString("Message"));
		}
		s.close();
		return results;
	}

	// Helper method to get time 7 days ago
	private static Date getMessageTimeBound() {
		return new Date(System.currentTimeMillis() - (7 * 1000 * 60 * 60 * 24));
	}

	/**
	 * Removes an account and all related data in the database depending on that
	 * account. Will not delete admin accounts.
	 * 
	 * @param targetAcc
	 *          The account to delete.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static void removeAccount(Account targetAcc) throws SQLException {
		// Prevent admin account deletions
		if (targetAcc.isAdmin()) {
			return;
		}
		Statement s = SQL.getStatement();
		// Remove all friend entries
		s.executeUpdate(String.format(
				"DELETE FROM Friends WHERE UserID = %d OR FriendID = %d;",
				targetAcc.getId(), targetAcc.getId()));
		// Remove all pending friend requests
		s.executeUpdate(String
				.format(
						"DELETE FROM PendingFriendRequests WHERE RequesterID = %d OR ReceiverID = %d;",
						targetAcc.getId(), targetAcc.getId()));
		// Remove all pending challenges
		s.executeUpdate(String
				.format(
						"DELETE FROM PendingChallenges WHERE RequesterID = %d OR ReceiverID = %d;",
						targetAcc.getId(), targetAcc.getId()));
		// Remove all notes
		s.executeUpdate(String.format(
				"DELETE FROM Notes WHERE RequesterID = %d OR ReceiverID = %d;",
				targetAcc.getId(), targetAcc.getId()));
		// Remove all history
		s.executeUpdate(String.format(
				"DELETE FROM HistoryEntries WHERE UserID = %d;", targetAcc.getId()));
		// Remove all achievements
		s.executeUpdate(String.format(
				"DELETE FROM EarnedAchievements WHERE UserID = %d;", targetAcc.getId()));
		// Remove all ratings
		s.executeUpdate(String.format("DELETE FROM Ratings WHERE UserID = %d;",
				targetAcc.getId()));
		// Remove all reports
		s.executeUpdate(String.format("DELETE FROM Reports WHERE UserID = %d;",
				targetAcc.getId()));
		// Remove all quizzes
		for (Quiz q : targetAcc.getAllCreatedQuizzes()) {
			removeQuiz(q);
		}
		// Remove account
		s.executeUpdate(String.format("DELETE FROM Accounts WHERE ID = %d;",
				targetAcc.getId()));
		s.close();
	}

	/**
	 * Removes a quiz and all related data in the database depending on that quiz.
	 * 
	 * @param q
	 *          The quiz to delete.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static void removeQuiz(Quiz q) throws SQLException {
		// Remove all questions first
		Statement s = SQL.getStatement();
		s.executeUpdate(String.format("DELETE FROM Questions WHERE QuizID = %d;",
				q.getId()));
		// Remove related pending challenges
		s.executeUpdate(String.format(
				"DELETE FROM PendingChallenges WHERE QuizID = %d;", q.getId()));
		// Remove all history
		removeQuizHistory(q);
		// Remove all taggings
		s.executeUpdate(String.format(
				"DELETE FROM QuizTaggings WHERE QuizID = %d;", q.getId()));
		// Remove all ratings
		s.executeUpdate(String.format("DELETE FROM Ratings WHERE QuizID = %d;",
				q.getId()));
		// Remove all reports
		s.executeUpdate(String.format("DELETE FROM Reports WHERE QuizID = %d;",
				q.getId()));
		// Remove quiz
		s.executeUpdate(String.format("DELETE FROM Quizzes WHERE ID = %d;",
				q.getId()));
		s.close();
	}

	/**
	 * Clears a quiz's history.
	 * 
	 * @param q
	 *          The quiz to delete history for.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static void removeQuizHistory(Quiz q) throws SQLException {
		Statement s = SQL.getStatement();
		s.executeUpdate(String.format(
				"DELETE FROM HistoryEntries WHERE QuizID = %d;", q.getId()));
		s.close();
	}

	/**
	 * Promotes an account to admin. Does nothing if the account is already an
	 * admin.
	 * 
	 * @param targetAcc
	 *          The account to promote.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static void promoteAccount(Account targetAcc) throws SQLException {
		// Prevent admin account promotions
		if (targetAcc.isAdmin()) {
			return;
		}
		Statement s = SQL.getStatement();
		s.executeUpdate(String.format(
				"UPDATE Accounts SET IsAdmin=TRUE WHERE ID = %d;", targetAcc.getId()));
		s.close();
	}

	/**
	 * Retrieves site statistics.
	 * 
	 * @return A SiteStatistics object representing global statistics about users
	 *         and quizzes.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static SiteStatistics getSiteStatistics() throws SQLException {
		return new SiteStatistics();
	}
}
