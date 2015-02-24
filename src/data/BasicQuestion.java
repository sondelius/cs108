package data;

import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;

import sql.SQL;

public class BasicQuestion extends Question {

	private static final String TYPE_TAG = "basicquestion";
	private static final int TYPE_ID = 101;
	private String question = "";
	private String answer = "";

	/**
	 * Gets the type ID associated with what kind of question this is. All
	 * question types (QuestionResponse, PictureQuestion) should reserve a unique
	 * type ID and override this method.
	 * 
	 * @return The type ID associated with this question.
	 */
	public static int getQuestionTypeId() {
		return TYPE_ID;
	}

	/**
	 * Gets the XML type tag associated with what kind of question this is. All
	 * question types should reserve a unique type tag and override this method.
	 * 
	 * @return the type tag associated with this question.
	 */
	public static String getQuestionTypeTag() {
		return TYPE_TAG;
	}

	public static String generateCreateQuestionHtml() {
		return "<div><h3>Please enter the question: </h3>"
				+ "<input type=\"text\" name=\"q" + TYPE_TAG
				+ "question\"><h3>What is the correct answer?</h3>"
				+ "<input type=\"text\" name=\"q" + TYPE_TAG
				+ "answer\" > </div>";
	}

	/**
	 * Handles a request to create a question once a user submits a form generated
	 * by generateCreateQuestionHtml.
	 * 
	 * @param req
	 *          The HttpServletRequest given to the create questions Servlet.
	 * @return The newly created question.
	 * @throws SQLException
	 *           If the SQL operations fail.
	 */
	public static Question createQuestion(HttpServletRequest req)
			throws SQLException {
		BasicQuestion q = new BasicQuestion();
		q.question = req.getParameter("q" + TYPE_TAG + "question");
		q.answer = req.getParameter("q" + TYPE_TAG + "answer");
		return q;
	}

	@Override
	protected String toDataString() {
		return "<q>" + question + "</q>" +
				"a" + answer + "</a>";
	}

	@Override
	public String generatePromptHtml() {
		return "<h3>" + question + "</h3>";
	}

	@Override
	public String generateAnswerAreaHtml() {
		String fieldName = TYPE_TAG + id;
		return "<div><input type=\"text\" name=\"" + fieldName
				+ "\" > </div>";
	}

	@Override
	public int getCorrectPoints(QuizSession session) throws SQLException {
		String their_answer = session.getResponseParam(TYPE_TAG + id);
		if (their_answer.toLowerCase().equals(answer.toLowerCase())) return 1;
		return 0;
	}

	@Override
	public String getCorrectResults(QuizSession session) throws SQLException {
		if (getCorrectPoints(session) == getTotalPointsPossible()) {
			return "<div><h2>Correct!</h2><p>The answer you provided is correct.</p></div>";
		} else {
			return "<div><p>Incorrect. The correct answer to &quot;" + question
					+ "&quot; is " + answer + ".</p></div>";
		}
	}

	@Override
	public int getTotalPointsPossible() {
		return 1;
	}

	@Override
	public String generateChangeQuestionHtml() {
		return "<div><h3>Please enter the question: </h3>"
				+ "<input type=\"text\" name=\"q" + TYPE_TAG
				+ "question\" value = \"" + question + "\">"
				+ "<h3>What is the correct answer?</h3>"
				+ "<input type=\"text\" name=\"q" + TYPE_TAG
				+ "answer\" value = \"" + answer + "\"> </div>";
	}

	@Override
	public void changeQuestion(HttpServletRequest req) throws SQLException {
		// Looking for POST parameters q12345questionPrompt and q12345correctAnswer
		question = req.getParameter("q" + TYPE_TAG + "question");
		answer = req.getParameter("q" + TYPE_TAG + "answer");
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
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadQuestionFromDB(String data) throws LoadException {
		String d = data;
		try {
			data = data.substring(data.indexOf("<q>") + "<q>".length());
			question = data.substring(0, data.indexOf("</q>"));
			data = data.substring(data.indexOf("<a>") + "<a>".length());
			answer = data.substring(0, data.indexOf("</a>"));
		} catch (Exception e){
			throw new LoadException("Error parsing question or answer from " + d);
		}
		
	}

}
