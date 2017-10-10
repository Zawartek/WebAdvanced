package ex01D;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ex01D.C;

public class CTest  extends TestCase {
	/** * Create the test case * * @param testName name of the test case */
	
	public CTest( String testName )
	{
		super( testName );
	}
	
	/** * @return the suite of tests being tested */
	public static Test suite()
	{
		return new TestSuite( CTest.class );
	}
	/** * Rigourous Test :-) */
	public void testApp()
	{
		int i = 0;
		StringBuffer s = new StringBuffer("abc");
		C.method1(i, s);
		assertEquals(i, 0);
		assertEquals(s.toString(), "abcd");
	}
}