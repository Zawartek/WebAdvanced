package ex01F;

import org.apache.log4j.Logger;

import dblab.hello.AppTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CircleTest  extends TestCase {
	/** * Create the test case * * @param testName name of the test case */
	
	public CircleTest( String testName )
	{
		super( testName );
	}
	
	/** * @return the suite of tests being tested */
	public static Test suite()
	{
		System.out.println("COUCOU");
		return new TestSuite( CircleTest.class );
	}
	/** * Rigourous Test :-) */
	public void testApp()
	{
		assertTrue( true );
	}
}