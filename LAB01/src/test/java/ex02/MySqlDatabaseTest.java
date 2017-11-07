package ex02;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MySqlDatabaseTest extends TestCase {
	/** * Create the test case * * @param testName name of the test case */

	public MySqlDatabaseTest(String testName) {
		super(testName);
	}

	/** * @return the suite of tests being tested */
	public static Test suite() {
		return new TestSuite(MySqlDatabaseTest.class);
	}

	/** * Rigourous Test :-) */
	public void testApp() {
		String db, user, pass, request;
		db = "sakila";
		user = "root";
		pass = "root";
		request = "SELECT last_name FROM actor;";
		// request = "UPDATE actor SET last_name = 'SIMOES' WHERE actor_id = 1;";
		MySqlDatabase dao = new MySqlDatabase(db, user, pass);

		try {
			dao.initialiseConnection();
			assertNotNull(dao.getConnection());
			assertNotNull(dao.getStatement());
			dao.executeRequest(request);
			assertNotNull(dao.getResultSet());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
			assertNull(dao.getConnection());
			assertNull(dao.getStatement());
			assertNull(dao.getResultSet());
		}
	}
}