package ex02;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ImportDatabaseTest extends TestCase {
	/** * Create the test case * * @param testName name of the test case */

	public ImportDatabaseTest(String testName) {
		super(testName);
	}

	/** * @return the suite of tests being tested */
	public static Test suite() {
		return new TestSuite(ImportDatabaseTest.class);
	}

	/** * Rigourous Test */
	public void testApp() {
		String database = "sakila";
		ImportDatabase importDB = new ImportDatabase(database);

		try {
			importDB.initImport("jdbc:mysql://localhost/sakila", "com.mysql.jdbc.Driver", "root", "root");
			importDB.importDB();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}