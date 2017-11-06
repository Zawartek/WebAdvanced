package ex02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySqlDatabase {

  public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	 
    private final String jdbcURL;
 
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    public MySqlDatabase(String database, String user, String password) {
    	jdbcURL = "jdbc:mysql://localhost/"+ database
    			+ "?user="+ user + "&password=" + password;
    }
    
    
    
    public void readData(String request) throws Exception {
        try {
            Class.forName(MYSQL_DRIVER);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(request);
            getResultSet(resultSet);
        }finally{
            close();
        }
    }
 
    private void getResultSet(ResultSet resultSet) throws Exception {
    	int nbCol=resultSet.getMetaData().getColumnCount();
    			
    	String data = "", delimiter="-", delimiterLine="", line="";
    	int[] colSizes = new int[nbCol];
    	for (int i=1; i<=nbCol;++i) {
    		colSizes[i-1] = resultSet.getMetaData().getColumnDisplaySize(i);
    		data = resultSet.getMetaData().getColumnName(i);
    		line+= String.format("%1$"+colSizes[i-1]+ "s", data);
    		delimiterLine += String.format("%1$"+colSizes[i-1]+ "s", "").replace(" ", delimiter);
    		if (i<nbCol) {
    			line+= " | ";
    			delimiterLine+=delimiter + delimiter + delimiter;
    		}
    	}
    	System.out.println(line);
    	System.out.println(delimiterLine);
    	
        while(resultSet.next()){
        	line = "";
        	for (int i=1;i<=nbCol;++i) {
        		data = resultSet.getString(i);
        		line+= String.format("%1$"+colSizes[i-1]+ "s", data);
        		if (i<nbCol) {
        			line+= " | ";
        		}
        	}
        	System.out.println(line);
        }
    }
 
    private void close(){
        try {
            if(resultSet!=null) resultSet.close();
            if(statement!=null) statement.close();
            if(connection!=null) connection.close();
        } catch(Exception e){}
    }
	
	public static void main(String[] args) throws Exception {
		String db, user, pass, request;
		db = "sakila";
		user = pass = "root";
		request = "SELECT last_name FROM actor;";
        MySqlDatabase dao = new MySqlDatabase(db, user, pass);
        dao.readData(request);
	}
}
