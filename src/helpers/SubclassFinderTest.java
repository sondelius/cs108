package helpers;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SubclassFinderTest {
	// A simple class hierarchy to test against

	private static class MyBaseClass {
	}

	private static class MySubClass1 extends MyBaseClass {
	}

	private static class MySubClass2 extends MySubClass1 {
	}

	@SuppressWarnings("unused")
	private static class MySubClass3 extends MySubClass2 {
	}

	/**
	 * Tests the ability to find that the subclasses of MyBaseClass are MySubClass
	 * 1 2 and 3.
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void test() {
		boolean containsSub1 = false, containsSub2 = false, containsSub3 = false;
		for (Class c : SubclassFinder.findSubclasses(MyBaseClass.class)) {
			if (c.getSimpleName().equals("MySubClass1")) {
				containsSub1 = true;
			} else if (c.getSimpleName().equals("MySubClass2")) {
				containsSub2 = true;
			} else if (c.getSimpleName().equals("MySubClass3")) {
				containsSub3 = true;
			}
		}
		assertTrue(containsSub1 && containsSub2 && containsSub3);
	}

}
