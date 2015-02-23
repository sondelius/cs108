package data;

import helpers.SubclassFinder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

import sql.SQL;

/**
 * Abstract class representing the framework for questions. Questions need to be
 * integrateable as the project progresses, so this generic framework allows
 * individual implementations of different question types to differ while
 * keeping the interface signatures the same.
 * 
 * @author Kevin
 *
 */
public abstract class Question {

	/**
	 * Custom exception to indicate that a given representation of a Question
	 * object could not be properly parsed or loaded from the SQL tables.
	 * 
	 * @author Kevin
	 *
	 */
	public static class LoadException extends Exception {
		private static final long serialVersionUID = 1L;

		public LoadException() {
			super();
		}

		public LoadException(String s) {
			super(s);
		}
	}

	/**
	 * Static method that, given an xml Node, tries to parse it as all types of
	 * questions and invokes the constructor of the appropriate subtype of
	 * question.
	 * 
	 * @param xmlNode
	 *          Any valid XML Node that for sure has a supported question.
	 * @return The parsed Question.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 * @throws LoadException
	 *           If none of the subtypes were able to parse the xmlNode.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Question loadQuestion(Node xmlNode) throws SQLException,
			LoadException {
		// Searches through all subtypes of Question and tries to parse the question
		// as if it were that subtype.
		for (Class c : SubclassFinder.findSubclasses(Question.class)) {
			try {
				if (xmlNode.getNodeName().equals(
						c.getMethod("getQuestionTypeTag").invoke(null))) {
					Question result = (Question) c.getConstructor().newInstance();
					result.parseFromXml(xmlNode);
					return result;
				}
			} catch (InvocationTargetException e) {
				if (e.getCause() instanceof SQLException) {
					throw (SQLException) e.getCause();
				}
			} catch (Exception ignored) {
			}
		}
		throw new LoadException();
	}

	/**
	 * Static method that, given a question ID, tries to look it up as all types
	 * of questions and invokes the constructor of the appropriate subtype of
	 * question.
	 * 
	 * @param id
	 *          The unique ID of the question to load.
	 * @return The parsed Question.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 * @throws LoadException
	 *           If none of the tables contained this ID.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Question loadQuestion(int id) throws SQLException,
			LoadException {
		Statement s = SQL.getStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM Questions WHERE ID = " + id
				+ " LIMIT 1;");
		if (!rs.next()) {
			throw new LoadException("Unable to find question with given ID [" + id
					+ "].");
		}
		return loadQuestionFromSQLRow(rs);
	}

	/**
	 * Static method that, given a row from an active ResultSet, tries to look it
	 * up as all types of questions and invokes the constructor of the appropriate
	 * subtype of question.
	 * 
	 * @param rs
	 *          The ResultSet, with the cursor in the position of the entry to be
	 *          processed, to get values from. Please ensure that ResultSet.next()
	 *          is called before invoking this method, since this method does not
	 *          invoke ResultSet.next().
	 * @return The parsed Question.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 * @throws LoadException
	 *           If none of the tables contained this ID.
	 */
	public static Question loadQuestionFromSQLRow(ResultSet rs)
			throws SQLException, LoadException {
		int qType = rs.getInt("QuestionType");
		String data = rs.getString("QuestionType");
		for (Class c : SubclassFinder.findSubclasses(Question.class)) {
			try {
				int questionType = (int) c.getMethod("getQuestionTypeId").invoke(null);
				if (questionType == qType) {
					Question result = (Question) c.getConstructor().newInstance();
					result.loadQuestionFromDB(data);
					return result;
				}
			} catch (InvocationTargetException e) {
				if (e.getCause() instanceof SQLException) {
					throw (SQLException) e.getCause();
				}
			} catch (Exception ignored) {
			}
		}
		throw new LoadException();
	}

	/**
	 * Gets the type ID associated with what kind of question this is. All
	 * question types (QuestionResponse, PictureQuestion) should reserve a unique
	 * type ID and override this method.
	 * 
	 * @return The type ID associated with this question.
	 */
	public static int getQuestionTypeId() {
		return -1;
	}

	/**
	 * Gets the XML type tag associated with what kind of question this is. All
	 * question types should reserve a unique type tag and override this method.
	 * 
	 * @return the type tag associated with this question.
	 */
	public static String getQuestionTypeTag() {
		return null;
	}

	/**
	 * Generates the HTML containing the prompt portion of a Question (ex. What is
	 * one plus one?).
	 * 
	 * @return The HTML string containing the prompt as a div as it should be
	 *         rendered when taking the quiz online.
	 */
	public abstract String generatePromptHtml();

	/**
	 * Generates the HTML containing the answer forminputs that a user will
	 * interact with to answer quiz questions. Note that when assigning POST
	 * parameter names for form inputs, please use names that will have a hard
	 * chance conflicting. An example includes appending the question ID and some
	 * unique value belonging to the answer choice (ex. q12345c1 for question with
	 * ID 12345 and answer choice 1). Note that this method should not contain the
	 * actual form tags but only contain the input tags associated with the UI.
	 * The final web page a user views may contain multiple questions on 1 page,
	 * which may have their own individual forms.
	 * 
	 * @return The HTML string containing the answering area as a div as it should
	 *         be rendered when taking the quiz online.
	 */
	public abstract String generateAnswerAreaHtml();

	/**
	 * Checks a QuizSession, after the HttpServletRequest has been processed, for
	 * correct answers and returns the number of points to award. The POST
	 * parameters assigned in generateAnswerAreaHtml should be within the
	 * QuizSession object. If the user does not provide these parameters by
	 * maliciously sending POST messages that are constructed outside of our
	 * webapp, assume the answer is wrong.
	 * 
	 * @param session
	 *          The QuizSession object holding all parameterized responses.
	 * @return The number of points scored.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public abstract int getCorrectPoints(QuizSession session) throws SQLException;

	/**
	 * Checks a QuizSession, after the HttpServletRequest has been processed, for
	 * correct answers and returns the HTML div containing a correct answer report
	 * for the question. This should include some form of "CORRECT" message if the
	 * provided answer is correct or some form of "INCORRECT. THE ANSWER IS: "
	 * message if the answer was not correct. This will be placed into pages such
	 * as the page after show correct right after mode and the final quiz results
	 * page.
	 * 
	 * @param session
	 *          The QuizSession object holding all parameterized responses.
	 * @return The HTML string containing a div with the content to show the user
	 *         about their performance on the question.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public abstract String getCorrectResults(QuizSession session)
			throws SQLException;

	/**
	 * Calculates the total number of points possible for the question. Some
	 * question types have a set max point value that all questions of that
	 * category can have (Picture Questions can only be 1 point) while others have
	 * variable point values (Question-Multiresponse gives 1 point per response
	 * box given, where the question can have infinitely many response boxes).
	 * 
	 * @return The total possible points available for this question.
	 */
	public abstract int getTotalPointsPossible();

	/**
	 * Generates the HTML form to present to users wishing to change a question
	 * that exists in a quiz already. Because the UI for editing different types
	 * of questions will clearly be different, each question type should implement
	 * this method to generate the appropriate UI. Make sure to encode a parameter
	 * holding the ID of the question being changed. Unline the AnswerArea HTML,
	 * this div can contain the form tags, as we can enforce that only one
	 * question can be changed at a time on the edit page.
	 * 
	 * @return The HTML string containing a div with a form to show the user a
	 *         custom interface for editing certain questions.
	 */
	public abstract String generateChangeQuestionHtml();

	/**
	 * Handles a request to change a question once a user submits a form generated
	 * by generateChangeQuestionHtml. Before changing questions, make sure to
	 * check that the logged in user is the creator of the quiz.
	 * 
	 * @param req
	 *          The HttpServletRequest given to the change questions Servlet.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public abstract void changeQuestion(HttpServletRequest req)
			throws SQLException;

	/**
	 * Generates the HTML form to present to users wishing to add a question to a
	 * quiz. Because the UI for creating different types of questions will clearly
	 * be different, each question type should implement this method to generate
	 * the appropriate UI. Unline the AnswerArea HTML, this div can contain the
	 * form tags, as we can enforce that only one question can be added at a time
	 * on the add question page.
	 * 
	 * @return The HTML string containing a div with a form to show the user a
	 *         custom interface for creating certain questions.
	 */
	public abstract String generateCreateQuestionHtml();

	/**
	 * Handles a request to create a question once a user submits a form generated
	 * by generateCreateQuestionHtml. Before creating questions, make sure to
	 * check that the logged in user is the creator of the quiz.
	 * 
	 * @param req
	 *          The HttpServletRequest given to the create questions Servlet.
	 * @return The newly created question.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public abstract Question createQuestion(HttpServletRequest req)
			throws SQLException;

	/**
	 * Attempts to parse an XML representation of the question, populating any
	 * relevant instance variables and writing to the SQL table if successfully
	 * parsed. Only if the node is entirely properly parsed should writes be made
	 * to the SQL tables.
	 * 
	 * @param xmlNode
	 *          The Node containing the entire question.
	 * @throws LoadException
	 *           If the XML cannot be properly parsed fully.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	protected abstract void parseFromXml(Node xmlNode) throws LoadException,
			SQLException;

	/**
	 * Attempts to load a question from its string representation for this type of
	 * question, populating any relevant instance variables if an entry is
	 * successfully found. No longer makes requests to the database; static loader
	 * that calls this method will retrieve data instead.
	 * 
	 * @param data
	 *          The data representing question and answers from the SQL database.
	 * @throws LoadException
	 *           If the requested ID cannot be found in the given table.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	protected abstract void loadQuestionFromDB(String data) throws LoadException;
}
