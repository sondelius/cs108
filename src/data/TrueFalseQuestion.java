package data;

import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import sql.SQL;

public class TrueFalseQuestion extends Question {
	private static final int QUESTION_TYPE = 12345;
	private static final String QUESTION_TAG = "tfquestion";

	private static final String SEPARATOR = "\u0031";

	private String questionPrompt;
	private boolean answerIsTrue;

	public static int getQuestionTypeId() {
		return QUESTION_TYPE;
	}

	public static String getQuestionTypeTag() {
		return QUESTION_TAG;
	}

	public static String generateCreateQuestionHtml() {
		// Creates a form with input q12345questionPrompt which holds the user's
		// entry for question prompt and input q12345correctAnswer which holds
		// whether the correct answer is true or false. These inputs contain q12345
		// prefix to prevent collision with the createQuestionHtml for other
		// subclasses of Question.
		return "<div><h3>Please enter the question prompt: </h3>"
				+ "<input type=\"text\" name=\"q" + QUESTION_TYPE
				+ "questionPrompt\"><h3>What is the correct answer?</h3>"
				+ "<input type=\"radio\" name=\"q" + QUESTION_TYPE
				+ "correctAnswer\" value=\"true\" checked>"
				+ "True<br><input type=\"radio\" name=\"q" + QUESTION_TYPE
				+ "correctAnswer\" value=\"false\">" + "False</div>";
	}

	public static Question createQuestion(HttpServletRequest req) {
		TrueFalseQuestion q = new TrueFalseQuestion();
		// Looking for POST parameters q12345questionPrompt and q12345correctAnswer
		q.questionPrompt = req.getParameter("q" + QUESTION_TYPE + "questionPrompt");
		q.answerIsTrue = Boolean.parseBoolean(req.getParameter("q" + QUESTION_TYPE
				+ "correctAnswer"));
		return q;
	}

	@Override
	protected String toDataString() {
		return questionPrompt + SEPARATOR + answerIsTrue;
	}

	@Override
	public String generatePromptHtml() {
		return "<h3>" + questionPrompt + "</h3>";
	}

	@Override
	public String generateAnswerAreaHtml() {
		// Generate a distinct field name for the response
		String fieldName = "q" + id + "answer";
		return "<div><input type=\"radio\" name=\"" + fieldName
				+ "\" value=\"true\" checked>"
				+ "True<br><input type=\"radio\" name=\"" + fieldName
				+ "\" value=\"false\">" + "False</div>";
	}

	@Override
	public int getCorrectPoints(QuizSession session) throws SQLException {
		// Look for that distinct field name here from generateAnswerAreaHtml
		return Boolean.parseBoolean(session.getResponseParam("q" + id + "answer")) == answerIsTrue ? 1
				: 0;
	}

	@Override
	public String getCorrectResults(QuizSession session) throws SQLException {
		if (getCorrectPoints(session) == getTotalPointsPossible()) {
			return "<div><h2>Correct!</h2><p>The answer you provided is correct.</p></div>";
		} else {
			return "<div><p>Incorrect. The correct answer to &quot;" + questionPrompt
					+ "&quot; is " + answerIsTrue + ".</p></div>";
		}
	}

	@Override
	public int getTotalPointsPossible() {
		return 1;
	}

	@Override
	public String generateChangeQuestionHtml() {
		return "<div><h3>Please enter the question prompt: </h3>"
				+ "<input type=\"text\" name=\"q" + QUESTION_TYPE
				+ "questionPrompt\" value = \"" + questionPrompt
				+ "\"><h3>What is the correct answer?</h3>"
				+ "<input type=\"radio\" name=\"q" + QUESTION_TYPE
				+ "correctAnswer\" value=\"true\"" + (answerIsTrue ? " checked" : "")
				+ ">" + "True<br><input type=\"radio\" name=\"q" + QUESTION_TYPE
				+ "correctAnswer\" value=\"false\"" + (!answerIsTrue ? " checked" : "")
				+ ">" + "False</div>";
	}

	@Override
	public void changeQuestion(HttpServletRequest req) throws SQLException {
		// Looking for POST parameters q12345questionPrompt and q12345correctAnswer
		questionPrompt = req.getParameter("q" + QUESTION_TYPE + "questionPrompt")
				.replace("'", "\\'");
		answerIsTrue = Boolean.parseBoolean(req.getParameter("q" + QUESTION_TYPE
				+ "correctAnswer"));
		// Update the old question's data field
		Statement s = SQL.getStatement();
		s.executeUpdate(String
				.format("UPDATE Questions SET Question='%s' WHERE ID = %d;",
						toDataString(), id));
		s.close();
	}

	@Override
	protected void parseFromXml(Element xmlNode) throws LoadException,
			SQLException {
		NodeList nodes = xmlNode.getElementsByTagName("question");
		if (nodes.getLength() != 1) {
			throw new LoadException(
					"Unable to locate 1 unique question prompt for parsing a TrueFalseQuestion.");
		}
		questionPrompt = ((Element) nodes.item(0)).getChildNodes().item(0)
				.getNodeValue();
		nodes = xmlNode.getElementsByTagName("answer");
		if (nodes.getLength() != 1) {
			throw new LoadException(
					"Unable to locate 1 unique answer for parsing a TrueFalseQuestion.");
		}
		try {
			answerIsTrue = Boolean.parseBoolean(((Element) nodes.item(0))
					.getChildNodes().item(0).getNodeValue());
		} catch (Exception e) {
			throw new LoadException(
					"Unable to parse answer for parsing a TrueFalseQuestion.");
		}
	}

	@Override
	protected void loadQuestionFromDB(String data) throws LoadException {
		String[] pieces = data.split(SEPARATOR);
		if (pieces.length != 2) {
			throw new LoadException(
					"Unable to parse TrueFalseQuestion: missing fields.");
		}
		questionPrompt = pieces[0];
		try {
			answerIsTrue = Boolean.parseBoolean(pieces[1]);
		} catch (Exception e) {
			throw new LoadException("Unable to parse answer of a TrueFalseQuestion.");
		}
	}

	// Functions for getting values specific to TrueFalseQuestions
	public String getPrompt() {
		return questionPrompt;
	}

	public boolean getAnswer() {
		return answerIsTrue;
	}
}
