package data;

import java.util.ArrayList;

/**
 * Represents an earnable achievement. Achievements should be assigned when
 * certain pages are accessed (for example the create quiz page for achievements
 * concerning number of created quizzes or the quiz results page for
 * achievements about high scores).
 * 
 * @author Kevin
 *
 */
public class Achievement {
	/**
	 * Modifies SQL tables to give a specific user a given achievement. Should
	 * only be called after checking that the user deserves the achievement and
	 * does not already have it. Generates an HTML banner div to show that the
	 * achievement was bestowed to the user.
	 * 
	 * @param u
	 *          The user to give the achievement to.
	 * @param a
	 *          The achievement to bestow.
	 * @return The HTML string containing a div to signify that the achievement
	 *         was earned. Will be inserted on the page where the achievement was
	 *         earned.
	 */
	public static String awardAchievement(Account u, Achievement a) {
		// TODO: Modify the SQL tables to give the user the provided achievement and
		// generate the HTML div to display to the user that an achievement was
		// earned.
		return null;
	}

	/**
	 * Gets an achievement object based on the achievement ID.
	 * 
	 * @param id
	 *          The ID of the achievement to get information about.
	 * @return An achievement object if found, null otherwise.
	 */
	public static Achievement getAchievementById(int id) {
		// TODO: Look through the allAchievements for the id'th index and return it;
		// if id is out of bounds, return null.
		return null;
	}

	private static ArrayList<Achievement> allAchievements;
	static {
		// TODO: Construct a single instance of each achievement (Amateur Author,
		// Prolific Author, ... etc) and put it into allAchievements. Make sure the
		// ID of any achievement in allAchievements matches the position in the
		// array (ex. the Achievement at position 0 should have ID 0).
	}

	private int id;
	private String name;
	private String iconSrc;
	private String tooltip;

	/**
	 * Private constructor of Achievement.
	 * 
	 * @param id
	 *          The ID of the achievement.
	 * @param name
	 *          The name of the achievement.
	 * @param iconSrc
	 *          The URL of the icon of the achievement.
	 * @param tooltip
	 *          The tooltip of the achievement.
	 */
	private Achievement(int id, String name, String iconSrc, String tooltip) {
		// TODO: assign fields to private instance variables
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIconSrc() {
		return iconSrc;
	}

	public String getTooltip() {
		return tooltip;
	}
}
