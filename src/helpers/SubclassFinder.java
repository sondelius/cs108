package helpers;

import java.io.File;
import java.util.ArrayList;

/**
 * Utility class for finding the subclasses of a given class. Used by Question
 * abstract class to find all implementations.
 * 
 * @author Kevin
 *
 */
public class SubclassFinder {
	@SuppressWarnings("rawtypes")
	/**
	 * Searches through a given class's package and finds all subclasses of the
	 * given class.
	 * @param c A class defined in this project.
	 * @return The list of subclasses of that class, in the same package of the provided class.
	 */
	public static ArrayList<Class> findSubclasses(Class c) {
		ArrayList<Class> results = new ArrayList<Class>();
		// Looks through the package for class files and checks for if they are
		// subclasses of the provided class.
		String packagePath = "/" + c.getPackage().getName().replace(".", "/");
		File packageDir = new File(SubclassFinder.class.getResource(packagePath)
				.getFile());
		if (packageDir.exists()) {
			for (String file : packageDir.list()) {
				if (file.endsWith(".class")) {
					try {
						Class potentialSubclass = Class.forName(c.getPackage().getName()
								+ "." + file.substring(0, file.length() - ".class".length()));
						if (!c.equals(potentialSubclass)) {
							Class superclass = potentialSubclass.getSuperclass();
							while (superclass != null) {
								if (superclass.equals(c)) {
									results.add(potentialSubclass);
									break;
								} else {
									superclass = superclass.getSuperclass();
								}
							}
						}
					} catch (Exception ignored) {
					}
				}
			}
		}
		return results;
	}
}
