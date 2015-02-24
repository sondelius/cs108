package data;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sql.SQL;
import testinghelpers.DummyHttpServletRequest;

public class TrueFalseQuestionTest {
	static class TestHttpServletRequest extends HttpServletRequestWrapper {
		public TestHttpServletRequest(HttpServletRequest request) {
			super(request);
		}
	}

	@Test
	public void testCreateQuestion() {
		try {
			// Make a fake quiz to add questions to
			SQL.init();
			Statement s = SQL.getStatement();
			String uniqueTestName = "Test Quiz" + System.currentTimeMillis();
			String query = "INSERT INTO Quizzes (Name,CreatorID,TimeCreated,CategoryID) SELECT '"
					+ uniqueTestName
					+ "', MAX(Accounts.ID), "
					+ SQL.convertDateToSQLDate(new Date())
					+ ", MAX(Categories.ID) FROM Accounts CROSS JOIN Categories;";
			s.executeUpdate(query);
			ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID() FROM Quizzes;");
			rs.next();
			int newlyCreatedQuizId = rs.getInt(1);
			Quiz testQuiz = Quiz.getQuizById(newlyCreatedQuizId);

			DummyHttpServletRequest req = new DummyHttpServletRequest();
			// Took these values from taking the output of generateCreateQuestionHtml
			// and putting it into a browser and finally wrapping it in a POST form,
			// inspecting POST parameters on the outbound HTTP request.
			req.setParameter("q12345questionPrompt", "Test Question");
			req.setParameter("q12345correctAnswer", "true");
			TrueFalseQuestion newlyCreatedQuestion = (TrueFalseQuestion) TrueFalseQuestion
					.createQuestion(req);
			newlyCreatedQuestion.addQuestionToQuiz(testQuiz);
			// Verify the question was created properly and parses properly.
			assertEquals(newlyCreatedQuestion.getPrompt(), "Test Question");
			assertEquals(newlyCreatedQuestion.getAnswer(), true);
			// Reload testQuiz and verify quiz question was added successfully
			testQuiz = Quiz.getQuizById(newlyCreatedQuizId);
			assertEquals(testQuiz.getTotalQuestions(), 1);
			// Fetch the first question of the quiz and verify the fields are as
			// expected.
			Question firstQuestion = testQuiz.getQuestion(0);
			assertTrue(firstQuestion instanceof TrueFalseQuestion);
			assertEquals(((TrueFalseQuestion) firstQuestion).getPrompt(),
					"Test Question");
			assertEquals(((TrueFalseQuestion) firstQuestion).getAnswer(), true);
			s.executeUpdate("DELETE FROM Questions WHERE ID = '"
					+ newlyCreatedQuestion.id + "';");
			s.executeUpdate("DELETE FROM Quizzes WHERE ID = '" + testQuiz.getId()
					+ "';");
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}

	@Test
	public void testLoadXMLQuestion() {
		try {
			// Make fake XML document
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			String xmlString = "<tfquestion><question>Test Question XML</question>"
					+ "<answer>true</answer></tfquestion>";
			InputStream stream = new ByteArrayInputStream(
					xmlString.getBytes(StandardCharsets.UTF_8));
			Document d = builder.parse(stream);
			d.getDocumentElement().normalize();
			Element xmlNode = (Element) d.getElementsByTagName(
					TrueFalseQuestion.getQuestionTypeTag()).item(0);

			// Make a fake quiz to add questions to
			SQL.init();
			Statement s = SQL.getStatement();
			String uniqueTestName = "Test Quiz" + System.currentTimeMillis();
			String query = "INSERT INTO Quizzes (Name,CreatorID,TimeCreated,CategoryID) SELECT '"
					+ uniqueTestName
					+ "', MAX(Accounts.ID), "
					+ SQL.convertDateToSQLDate(new Date())
					+ ", MAX(Categories.ID) FROM Accounts CROSS JOIN Categories;";
			s.executeUpdate(query);
			ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID() FROM Quizzes;");
			rs.next();
			int newlyCreatedQuizId = rs.getInt(1);
			Quiz testQuiz = Quiz.getQuizById(newlyCreatedQuizId);

			TrueFalseQuestion newlyCreatedQuestion = (TrueFalseQuestion) Question
					.loadQuestion(xmlNode);
			newlyCreatedQuestion.addQuestionToQuiz(testQuiz);
			// Verify the question was created properly and parses properly.
			assertEquals(newlyCreatedQuestion.getPrompt(), "Test Question XML");
			assertEquals(newlyCreatedQuestion.getAnswer(), true);
			// Reload testQuiz and verify quiz question was added successfully
			testQuiz = Quiz.getQuizById(newlyCreatedQuizId);
			assertEquals(testQuiz.getTotalQuestions(), 1);
			// Fetch the first question of the quiz and verify the fields are as
			// expected.
			Question firstQuestion = testQuiz.getQuestion(0);
			assertTrue(firstQuestion instanceof TrueFalseQuestion);
			assertEquals(((TrueFalseQuestion) firstQuestion).getPrompt(),
					"Test Question XML");
			assertEquals(((TrueFalseQuestion) firstQuestion).getAnswer(), true);
			s.executeUpdate("DELETE FROM Questions WHERE ID = '"
					+ newlyCreatedQuestion.id + "';");
			s.executeUpdate("DELETE FROM Quizzes WHERE ID = '" + testQuiz.getId()
					+ "';");
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}

	@Test
	public void testGetCorrectPoints() {
		try {
			// Make a fake quiz to add questions to
			SQL.init();
			Statement s = SQL.getStatement();
			String uniqueTestName = "Test Quiz" + System.currentTimeMillis();
			String query = "INSERT INTO Quizzes (Name,CreatorID,TimeCreated,CategoryID) SELECT '"
					+ uniqueTestName
					+ "', MAX(Accounts.ID), "
					+ SQL.convertDateToSQLDate(new Date())
					+ ", MAX(Categories.ID) FROM Accounts CROSS JOIN Categories;";
			s.executeUpdate(query);
			ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID() FROM Quizzes;");
			rs.next();
			int newlyCreatedQuizId = rs.getInt(1);
			Quiz testQuiz = Quiz.getQuizById(newlyCreatedQuizId);

			DummyHttpServletRequest req = new DummyHttpServletRequest();
			// Took these values from taking the output of generateCreateQuestionHtml
			// and putting it into a browser and finally wrapping it in a POST form,
			// inspecting POST parameters on the outbound HTTP request.
			req.setParameter("q12345questionPrompt", "Test Question");
			req.setParameter("q12345correctAnswer", "true");
			TrueFalseQuestion newlyCreatedQuestion = (TrueFalseQuestion) TrueFalseQuestion
					.createQuestion(req);
			newlyCreatedQuestion.addQuestionToQuiz(testQuiz);

			QuizSession session = new QuizSession(null, testQuiz, false);
			// Try a correct question
			req = new DummyHttpServletRequest();
			req.setParameter("q" + newlyCreatedQuestion.id + "answer", "true");
			session.addNewResponses(req);
			assertEquals(newlyCreatedQuestion.getCorrectPoints(session), 1);
			assertTrue(newlyCreatedQuestion.getCorrectResults(session).contains(
					"Correct"));

			// Try an incorrect question
			session = new QuizSession(null, testQuiz, false);
			req = new DummyHttpServletRequest();
			req.setParameter("q" + newlyCreatedQuestion.id + "answer", "false");
			session.addNewResponses(req);
			assertEquals(newlyCreatedQuestion.getCorrectPoints(session), 0);
			assertTrue(newlyCreatedQuestion.getCorrectResults(session).contains(
					"Incorrect"));

			// Cleanup
			s.executeUpdate("DELETE FROM Questions WHERE ID = '"
					+ newlyCreatedQuestion.id + "';");
			s.executeUpdate("DELETE FROM Quizzes WHERE ID = '" + testQuiz.getId()
					+ "';");
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}

	@Test
	public void testChangeQuestion() {
		try {
			// Make a fake quiz to add questions to
			SQL.init();
			Statement s = SQL.getStatement();
			String uniqueTestName = "Test Quiz" + System.currentTimeMillis();
			String query = "INSERT INTO Quizzes (Name,CreatorID,TimeCreated,CategoryID) SELECT '"
					+ uniqueTestName
					+ "', MAX(Accounts.ID), "
					+ SQL.convertDateToSQLDate(new Date())
					+ ", MAX(Categories.ID) FROM Accounts CROSS JOIN Categories;";
			s.executeUpdate(query);
			ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID() FROM Quizzes;");
			rs.next();
			int newlyCreatedQuizId = rs.getInt(1);
			Quiz testQuiz = Quiz.getQuizById(newlyCreatedQuizId);

			// Add a dummy question
			DummyHttpServletRequest req = new DummyHttpServletRequest();
			req.setParameter("q12345questionPrompt", "Test Question");
			req.setParameter("q12345correctAnswer", "true");
			TrueFalseQuestion newlyCreatedQuestion = (TrueFalseQuestion) TrueFalseQuestion
					.createQuestion(req);
			newlyCreatedQuestion.addQuestionToQuiz(testQuiz);

			// Change the dummy question
			req = new DummyHttpServletRequest();
			req.setParameter("q12345questionPrompt", "Changed the question to this");
			req.setParameter("q12345correctAnswer", "false");
			newlyCreatedQuestion.changeQuestion(req);

			// Check local question for changes; also reload the question to see if
			// database saw changes.
			assertEquals(newlyCreatedQuestion.getPrompt(),
					"Changed the question to this");
			assertEquals(newlyCreatedQuestion.getAnswer(), false);
			newlyCreatedQuestion = (TrueFalseQuestion) Question
					.loadQuestion(newlyCreatedQuestion.getQuestionId());
			assertEquals(newlyCreatedQuestion.getPrompt(),
					"Changed the question to this");
			assertEquals(newlyCreatedQuestion.getAnswer(), false);

			// Cleanup
			s.executeUpdate("DELETE FROM Questions WHERE ID = '"
					+ newlyCreatedQuestion.id + "';");
			s.executeUpdate("DELETE FROM Quizzes WHERE ID = '" + testQuiz.getId()
					+ "';");
			s.close();
			SQL.cleanup();
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}

}
