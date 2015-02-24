package data;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sql.SQL;

public class QuestionTest {
	/**
	 * Dummy question class used for testing the generic reflections routing in
	 * Question for creating new instances of Question objects.
	 * 
	 * @author Kevin
	 *
	 */
	public static class DummyQuestion extends Question {
		// Boolean variables are defaultly initialized to false. By checking that
		// the value is true, we can confirm the proper handler function was
		// executed.
		private boolean xmlCalled;
		private boolean loadCalled;

		public static int getQuestionTypeId() {
			return 9999;
		}

		public static String getQuestionTypeTag() {
			return "dummyquestion";
		}

		public String generatePromptHtml() {
			return null;
		}

		public String generateAnswerAreaHtml() {
			return null;
		}

		public int getCorrectPoints(QuizSession session) throws SQLException {
			return 0;
		}

		public String getCorrectResults(QuizSession session) throws SQLException {
			return null;
		}

		public int getTotalPointsPossible() {
			return 0;
		}

		public String generateChangeQuestionHtml() {
			return null;
		}

		public void changeQuestion(HttpServletRequest req) throws SQLException {
		}

		public static String generateCreateQuestionHtml() {
			return null;
		}

		protected void parseFromXml(Element xmlNode) throws LoadException,
				SQLException {
			// Signify the parsing happened
			xmlCalled = true;
		}

		protected void loadQuestionFromDB(String data) throws LoadException {
			// Signify the loading happened
			loadCalled = true;
		}

		@Override
		public String toDataString() {
			return null;
		}
	}

	/**
	 * Tests XML routing by using the DummyQuestion and parsing a bit of fake XML
	 * to force the routing to select DummyQuestion as the proper handler for
	 * construction.
	 */
	@Test
	public void testLoadXMLRouting() {
		try {
			// Create a fake node object with tag <dummyquestion>
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			String xmlString = "<dummyquestion><text>Hello</text></dummyquestion>";
			InputStream stream = new ByteArrayInputStream(
					xmlString.getBytes(StandardCharsets.UTF_8));
			Document d = builder.parse(stream);
			d.getDocumentElement().normalize();
			Element n = (Element) d.getElementsByTagName("dummyquestion").item(0);
			// See that the static loading worked
			DummyQuestion q = (DummyQuestion) Question.loadQuestion(n);
			assertTrue(q.xmlCalled);
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}

	/**
	 * Tests load routing by using the DummyQuestion and creating a fake question
	 * entry in the database and then loads the newly created question to force
	 * the routing to select DummyQuestion as the proper handler for construction.
	 */
	@Test
	public void testLoadQuestionRouting() {
		try {
			// Create some dummy entries to satisfy FOREIGN KEY constraints.
			SQL.init();
			Statement s = SQL.getStatement();
			String uniqueTestName = "Test Quiz" + System.currentTimeMillis();
			String query = "INSERT INTO Quizzes (Name,CreatorID,TimeCreated,CategoryID) SELECT '"
					+ uniqueTestName
					+ "', MAX(Accounts.ID), "
					+ SQL.convertDateToSQLDate(new Date())
					+ ", MAX(Categories.ID) FROM Accounts CROSS JOIN Categories;";
			s.executeUpdate(query);
			// Make a new question of type DummyQuestion
			query = "INSERT INTO Questions (QuizID,QuestionType,QuestionNumber,Question) SELECT ID, "
					+ DummyQuestion.getQuestionTypeId()
					+ ", 0, '' FROM Quizzes WHERE Name = '" + uniqueTestName + "';";
			s.executeUpdate(query);
			ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID() FROM Questions;");
			rs.next();
			int newlyCreatedId = rs.getInt(1);
			// Load the newly created question and see if it properly calls the load.
			DummyQuestion q = (DummyQuestion) Question.loadQuestion(newlyCreatedId);
			assertTrue(q.loadCalled);
			// Table cleanup.
			s.executeUpdate("DELETE FROM Questions WHERE ID = " + newlyCreatedId
					+ ";");
			s.executeUpdate("DELETE FROM Quizzes WHERE Name = '" + uniqueTestName
					+ "';");
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}
}
