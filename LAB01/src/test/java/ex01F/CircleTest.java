package ex01F;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CircleTest extends TestCase {
	/** * Create the test case * * @param testName name of the test case */

	public CircleTest(String testName) {
		super(testName);
	}

	/** * @return the suite of tests being tested */
	public static Test suite() {
		return new TestSuite(CircleTest.class);
	}

	/** * Rigourous Test :-) */
	public void testApp() {
		Circle c1 = new Circle(0, 0, 5);
		Circle c2 = new Circle(0, 0, 3);
		String c1string;
		assertNotNull(c1);
		c1string = c1 + "";
		assertEquals(c1string, c1.toString());

		assertFalse(c1.toString().equals(c2.toString()));

	}
}