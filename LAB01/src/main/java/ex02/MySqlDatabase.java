package ex02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySqlDatabase {

	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	private static final String LINE_DELIMITER="-";
	private static final String COLUMN_DELIMITER="|";
	
	private final String jdbcURL;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	

	/**
	 * Construction of the class
	 * Create the jdbc URL base on the parameters
	 * @param database
	 * @param user
	 * @param password
	 */
	public MySqlDatabase(String database, String user, String password) {
		jdbcURL = "jdbc:mysql://localhost/" + database + "?user=" + user + "&password=" + password;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public Statement getStatement() {
		return statement;
	}
	
	public ResultSet getResultSet() {
		return resultSet;
	}
	
	/**
	 * Execute the request, print the result and release the database objects
	 * @param request
	 * @throws Exception
	 */
	public void readData(String request) {
		try {
			initialiseConnection();
			executeRequest(request);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void initialiseConnection() throws Exception{
		Class.forName(MYSQL_DRIVER);
		connection = DriverManager.getConnection(jdbcURL);
		statement = connection.createStatement();
	}
	
	/**
	 * Execute the request and print the result
	 * @param request
	 * @throws Exception
	 */
	public void executeRequest(String request) throws Exception {
		if (request.toUpperCase().startsWith("SELECT")) {
			resultSet = statement.executeQuery(request);
			printResult(resultSet);
		} else {
			System.out.println(statement.executeUpdate(request));
		}
	}

	/**
	 * Release all the database objects (resultSet, statement, connection)
	 */
	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
				resultSet = null;
			}
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Print the result of the request in the console
	 * @param resultSet
	 * @throws Exception
	 */
	private void printResult(ResultSet resultSet) throws Exception {
		int nbCol = resultSet.getMetaData().getColumnCount();
		int[] colSizes = new int[nbCol];
		String line = "", delimiterLine;

		line = getHeader(resultSet, colSizes, nbCol);
		System.out.println(line);
		
		delimiterLine = String.format("%1$" + line.length() + "s", "")
				.replace(" ", LINE_DELIMITER);
		System.out.println(delimiterLine);
		
		while (resultSet.next()) {
			line = getLine(resultSet, colSizes, nbCol);
			System.out.println(line);
		}
	}
	
	/**
	 * Return the header (Column names) as a String
	 * @param resultSet
	 * @param colSizes
	 * @param nbCol
	 * @return line
	 * @throws Exception
	 */
	private String getHeader(ResultSet resultSet, int[] colSizes, int nbCol) throws Exception {
		String data="", line="";
		for (int i = 1; i <= nbCol; ++i) {
			colSizes[i - 1] = resultSet.getMetaData().getColumnDisplaySize(i);
			if (colSizes[i-1]<resultSet.getMetaData().getColumnName(i).length()) {
				colSizes[i-1] = resultSet.getMetaData().getColumnName(i).length();
			}
			data = resultSet.getMetaData().getColumnName(i);
			line += String.format("%1$" + colSizes[i - 1] + "s", data);
			if (i < nbCol) {
				line = addColumnDelimiter(line);
			}
		}
		return line;
	}
	
	/**
	 * Return the line of result as a String
	 * @param resultSet
	 * @param colSizes
	 * @param nbCol
	 * @return line
	 * @throws Exception
	 */
	private String getLine(ResultSet resultSet, int[] colSizes, int nbCol) throws Exception {
		String data="", line="";
		for (int i = 1; i <= nbCol; ++i) {
			data = resultSet.getString(i);
			line += String.format("%1$" + colSizes[i - 1] + "s", data);
			if (i < nbCol) {
				line = addColumnDelimiter(line);
			}
		}
		return line;
	}
	
	/**
	 * Add the column delimiter to the line
	 * @param line
	 * @return line
	 */
	private String addColumnDelimiter(String line) {
		line += " "+ COLUMN_DELIMITER +" ";
		return line;
	}


	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		String db, user, pass, request;
		db = "sakila";
		user = "root";
		pass = "root";
		request = "SELECT * FROM actor;";
		// request = "UPDATE actor SET last_name = 'SIMOES' WHERE actor_id = 1;";
		MySqlDatabase dao = new MySqlDatabase(db, user, pass);
		dao.readData(request);
	}
}
