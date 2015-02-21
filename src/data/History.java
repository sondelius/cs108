package data;

import java.util.Date;

/**
 * Represents a history recording of a previous attempt to take a quiz.
 * 
 * @author Kevin
 *
 */
public class History {
	private int accountId;
	private int quizId;
	private int score;
	private Date timeStarted;
	private Date timeTaken;

	/**
	 * Constructor to represent a History object. Should only be called from
	 * within {@link Account#getHistory()} and {@link Quiz#getHistory()}, which
	 * parses HistoryEntry rows from the SQL tables.
	 * 
	 * @param accountId
	 *          The ID of the account that took the test.
	 * @param quizId
	 *          The ID of the quiz taken.
	 * @param score
	 *          The score of the history entry.
	 * @param timeStarted
	 *          The time the quiz was taken.
	 * @param timeTaken
	 *          The total time the quiz took.
	 */
	public History(int accountId, int quizId, int score, Date timeStarted,
			Date timeTaken) {
		this.accountId = accountId;
		this.quizId = quizId;
		this.score = score;
		this.timeStarted = timeStarted;
		this.timeTaken = timeTaken;
	}

	// TODO: Each of the methods below fetches information about accounts that is
	// dynamically changing and should be fetched upon request from the SQL table.
	// Write Javadocs and implementations that load data or constructs objects
	// that load data fresh from the SQL database.

	public Quiz getQuiz() {
		return null;
	}

	public Account getAccount() {
		// TODO: load account using Account.getAccountById
		return null;
	}
}
