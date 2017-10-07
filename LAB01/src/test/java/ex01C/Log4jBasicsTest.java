package ex01C;
import org.apache.log4j.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Log4jBasicsTest extends TestCase
{
	protected static Logger log = Logger.getLogger(Log4jBasics.class);

	public Log4jBasicsTest( String testName )
	{
		super( testName );
	}

	/** * @return the suite of tests being tested */
	public static Test suite()
	{
		System.out.println("COUCOU");
		return new TestSuite( Log4jBasicsTest.class );
	}
	/** * Rigourous Test :-) */
	public void testApp()
	{
		assertTrue( true );
	}
}
