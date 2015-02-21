package data;

import helpers.SubclassFinder;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

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
				return (Question) c.getConstructor(Node.class).newInstance(xmlNode);
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
	public static Question loadquestion(int id) throws SQLException,
			LoadException {
		for (Class c : SubclassFinder.findSubclasses(Question.class)) {
			try {
				return (Question) c.getConstructor(Integer.class).newInstance(id);
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
	 * Reserves the next available question ID in the SQL table.
	 * 
	 * @return the reserved ID.
	 */
	public static int reserveNextQuestionId() throws SQLException {
		// TODO: Get the next ID by simply performing a Union operation on all
		// tables containing questions (QRQuestions, MCQuestions, etc) and getting
		// the MAX value of ID and incrementing by 1, returning that value.
		return 0;
	}

	/**
	 * Generic constructor for Question objects when parsing them from XML. This
	 * should not be modified. To specify the specific behavior of parsing a
	 * specialized question, please implement the parseFromXml method.
	 * 
	 * @param xmlNode
	 *          The node containing the question data from the XML document.
	 * @throws Exception
	 *           If the parseFromXml hits any issues.
	 * @see data.Question#parseFromXml(Node) parseFromXml
	 */
	public Question(Node xmlNode) throws Exception {
		parseFromXml(xmlNode);
	}

	/**
	 * Generic constructor for loading Question objects from the SQL database.
	 * This should not be modified. To specify the specific behavior of reading an
	 * SQL table and loading in relevant data, please implement the
	 * loadQuestionFromDB method.
	 * 
	 * @param id
	 *          The ID of the question to load.
	 * @throws Exception
	 *           If the loadQuestionFromDB hits any issues.
	 * @see data.Question#loadQuestionFromDB(int) loadQuestionFromDB
	 */
	public Question(int id) throws Exception {
		loadQuestionFromDB(id);
	}

	/**
	 * Gets the type ID associated with what kind of question this is. All
	 * question types (QuestionResponse, PictureQuestion) should reserve a unique
	 * type ID.
	 * 
	 * @return The type ID associated with this question.
	 */
	public abstract int getQuestionTypeId();

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
	 * Attempts to load a question from SQL table for this type of question,
	 * populating any relevant instance variables if an entry is successfully
	 * found.
	 * 
	 * @param id
	 *          The ID of the question to look for.
	 * @throws LoadException
	 *           If the requested ID cannot be found in the given table.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	protected abstract void loadQuestionFromDB(int id) throws LoadException,
			SQLException;
}
