package ex01E;

import dblab.hello.AppTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CTest  extends TestCase {
	/** * Create the test case * * @param testName name of the test case */
	
	public CTest( String testName )
	{
		super( testName );
	}
	
	/** * @return the suite of tests being tested */
	public static Test suite()
	{
		System.out.println("COUCOU");
		return new TestSuite( CTest.class );
	}
	/** * Rigourous Test :-) */
	public void testApp()
	{
		assertTrue( true );
	}
}