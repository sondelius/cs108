package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a quiz, a collection of questions.
 * 
 * @author Kevin
 *
 */
public class Quiz {
	/**
	 * Class that represents aggregate statistics about Quiz histories. Data is
	 * fetched upon the instantiation of this class and then is stored locally.
	 * 
	 * @author Kevin
	 *
	 */
	public static class QuizStatistics {
		private int totalTakes;
		private double averageScore;
		private double averageRating;
		private Date averageTimeTaken;

		/**
		 * Constructor for QuizStatistics.
		 * 
		 * @param id
		 *          The ID of the quiz to fetch stats for.
		 */
		private QuizStatistics(int id) throws SQLException {
			// TODO: Make statistics by fetching data about HistoryEntries from SQL.
			// MySQL has aggregate commands such as COUNT, SUM, etc.
		}

		public int getTotalTakes() {
			return totalTakes;
		}

		public double getAverageScore() {
			return averageScore;
		}

		public double getAverageRating() {
			return averageRating;
		}

		public Date getAverageTimeTaken() {
			return averageTimeTaken;
		}
	}

	private int id;
	private String name;
	private int creatorId;
	private Date timeCreated;
	private int categoryId;
	private boolean randomOrder;
	private boolean multiPage;
	private boolean immediateCorrection;
	private boolean allowPractice;
	private int totalQuestions = -1;

	/**
	 * Private constructor.
	 * 
	 * @param id
	 *          The ID of the quiz.
	 * @param name
	 *          The name of the quiz.
	 * @param creatorId
	 *          The account ID of the creator.
	 * @param timeCreated
	 *          The time the quiz was created.
	 * @param categoryId
	 *          The ID of the category of the quiz.
	 * @param randomOrder
	 *          Whether or not the questions will be displayed in a random order.
	 * @param multiPage
	 *          Whether or not questions will be displayed one after another.
	 * @param immediateCorrection
	 *          Whether or not questions will display correct/incorrect status
	 *          right after submission in multipage mode.
	 * @param allowPractice
	 *          Whether or not the quiz allows pratice runs.
	 */
	private Quiz(int id, String name, int creatorId, Date timeCreated,
			int categoryId, boolean randomOrder, boolean multiPage,
			boolean immediateCorrection, boolean allowPractice) {
		this.id = id;
		this.name = name;
		this.creatorId = creatorId;
		this.timeCreated = timeCreated;
		this.categoryId = categoryId;
		this.randomOrder = randomOrder;
		this.multiPage = multiPage;
		this.immediateCorrection = immediateCorrection;
		this.allowPractice = allowPractice;
	}

	/**
	 * Load a quiz from SQL tables by ID.
	 * 
	 * @param id
	 *          The ID of the Quiz to find.
	 * @return A Quiz representation of the entry, or null if not found.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Quiz getQuizById(int id) throws SQLException {
		// TODO: Make requests to the SQL table and create a Quiz representation of
		// the found entry if any. Use the private constructor to create a new
		// instance of Quiz after querying the SQL table and finding a result.
		return null;
	}

	/**
	 * Searches the quiz tables to find and load all relevant quizzes. Parameters
	 * provided will be used in an AND conjunction; to designate that a given
	 * parameter is not relevant (ex. disregard creator when searching), provide
	 * null for said parameter.
	 * 
	 * @param name
	 *          The exact name of the quiz.
	 * @param category
	 *          The exact category of the quiz.
	 * @param creator
	 *          The exact username of the creator.
	 * @param tags
	 *          The set of tags that the quiz should be tagged with.
	 * @param createdAfter
	 *          The date the quiz should be created after.
	 * @param sortByPopularity
	 *          Whether or not to sort the results by popularity or not.
	 * @param maxEntries
	 *          The maximum number of entries to return.
	 * @return An ArrayList of Quizzes representing the entries that matched the
	 *         search criteria.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static ArrayList<Quiz> findQuizzes(String name, String category,
			String creator, ArrayList<String> tags, Date createdAfter,
			boolean sortByPopularity, int maxEntries) throws SQLException {
		// TODO: Generate the appropriate MySQL query that satisfies the above
		// constraints, then for each result, invoke the Quiz constructor to create
		// a new instance of Quiz. Place all these instances in an arraylist and
		// return.
		return null;
	}

	/**
	 * Attempts to create a new, empty quiz.
	 * 
	 * @param name
	 *          The name of the quiz.
	 * @param creator
	 *          The account that created the quiz.
	 * @param category
	 *          The category that the quiz was put under.
	 * @param tags
	 *          The tags this quiz was tagged with.
	 * @param randomOrder
	 *          Whether or not the questions will be displayed in a random order.
	 * @param multiPage
	 *          Whether or not questions will be displayed one after another.
	 * @param immediateCorrection
	 *          Whether or not questions will display correct/incorrect status
	 *          right after submission in multipage mode.
	 * @param allowPractice
	 *          Whether or not the quiz allows pratice runs.
	 * @return The Quiz object representing the newly created quiz.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Quiz createQuiz(String name, Account creator, String category,
			ArrayList<String> tags, boolean randomOrder, boolean multiPage,
			boolean immediateCorrection, boolean allowPractice) throws SQLException {
		// TODO: Create an entry in the SQL tables for this new quiz. For category
		// and tags, use the methods in Category and Tags to create new categories
		// and tags if needed, or to fetch existing tags.
		return null;
	}

	/**
	 * Retrieves the nth question in this quiz.
	 * 
	 * @param questionNumber
	 *          The index of the question in the quiz.
	 * @return The question if found or null if out of bounds.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public Question getQuestion(int questionNumber) throws SQLException {
		// TODO: Query the QuizQuestions table for the nth question of this quiz,
		// and then invoke the static functions in Question to resolve the question
		// by ID since QuizQuestions includes Question ID.
		return null;
	}

	/**
	 * Gets the total number of questions the quiz has. Caches result locally upon
	 * first fetch.
	 * 
	 * @return The total number of questions the quiz has.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public int getTotalQuestions() throws SQLException {
		if (totalQuestions == -1) {
			// TODO: Query the QuizQuestions table to get the total number of
			// questions this quiz has by using aggregate keywords like COUNT. Store
			// this value in totalQuestions and keep it locally.
		}
		return totalQuestions;
	}

	/**
	 * Adds a question to this quiz. Should be called after Question has created
	 * the question entry in the table via createQuestion.
	 * 
	 * @param q
	 *          The question to add to this quiz.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public void addQuestion(Question q) throws SQLException {
		// TODO: Query to make sure the question exists in the tables and then use
		// getTotalQuestions to find the total number of questions, setting this
		// question's index to that value when creating a new entry in
		// QuizQuestions. The actual question q should already exist in the
		// database; this method simply adds an entry into QuizQuestions,
		// associating this quiz with that question. Make sure that this question is
		// not already in the quiz before adding!
	}

	public String getName() {
		return name;
	}

	/**
	 * Gets the creator of this quiz.
	 * 
	 * @return The creator of this quiz.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public Account getCreator() throws SQLException {
		// TODO: Call Account.getAccountById using this question's creatorId.
		return null;
	}

	/**
	 * Gets all history entries for this quiz.
	 * 
	 * @return An ArrayList of past attempts to take this quiz.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public ArrayList<History> getHistory() throws SQLException {
		// TODO: fetch all history for this quiz and parse the entries of the
		// History table, constructing new History objects and populating a result
		// arraylist. See SQL.convertSQLDateToDate on parsing SQL Date objects.
		return null;
	}

	/**
	 * Gets all attempts by a given user to take this quiz.
	 * 
	 * @param a
	 *          The account to constrain search by.
	 * @return An ArrayList of the user's past attempts to take this quiz.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public ArrayList<History> getHistory(Account a) throws SQLException {
		// TODO: Do the same as getHistory(), except constrain the SQL command on
		// having account ID provided in a.
		return null;
	}

	/**
	 * Gets the top performers in the last day.
	 * 
	 * @return An ArrayList of the histories of the top performers in the last
	 *         day.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public ArrayList<History> getDayTopPerformers() throws SQLException {
		// TODO: Do the same as getHistory(), except constrain the SQL command on
		// having a date after 12 AM of the current day. To get the current day, use
		// new Date() and simply set the hours minute seconds fields to 0 to get the
		// beginning of the current day. To convert from Date to SQL date
		// representation, use SQL.convertDateToSQLDate.
		return null;
	}

	/**
	 * Calculates statistics about the quiz.
	 * 
	 * @return A QuizStatistics object holding calculated statistics.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public QuizStatistics getStatistics() throws SQLException {
		// TODO: Return a newly constructed QuizStatistics object by invoking its
		// constructor, which should take care of calculating all the numbers.
		return null;
	}

	/**
	 * Gets the category of the quiz.
	 * 
	 * @return The name of the containing category.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public String getCategory() throws SQLException {
		// TODO: take the categoryId and search for that entry in the Categories
		// table.
		return null;
	}

	/**
	 * Gets the tags of the quiz.
	 * 
	 * @return An ArrayList of tags the quiz was tagged with.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public ArrayList<String> getTags() throws SQLException {
		// TODO: Find all tags in QuizTaggings that this quiz has and then find the
		// corresponding tag names in Tags. The SQL JOIN operator allows for easy
		// accumulation of tags.
		return null;
	}

	// TODO: Write JavaDoc entries for all locally-stored getters below.
	public boolean isRandomOrder() {
		return randomOrder;
	}

	public boolean isMultiPage() {
		return multiPage;
	}

	public boolean isImmediateCorrection() {
		return immediateCorrection;
	}

	public boolean isAllowPractice() {
		return allowPractice;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public int getId() {
		return id;
	}
}
