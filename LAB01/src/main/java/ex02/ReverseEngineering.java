package ex02;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReverseEngineering {

	public static String jdbcDriver = "com.mysql.jdbc.Driver";
	private static String jdbcURL = "jdbc:mysql://localhost/sakila";
	private static String user = "root";
	private static String password = "root";

	public static void main(String[] args) {
		try {
			SQLExec sqlExec = new SQLExec(jdbcURL, jdbcDriver, user, password);
			sqlExec.initialiseConnection();
			DatabaseMetaData metaData = sqlExec.getConnection().getMetaData();
			ResultSet rsTable = metaData.getTables(null, null, null, new String[] { "TABLE" });

			ArrayList<String> tableName = new ArrayList<String>();
			int i = 0;

			while (rsTable.next()) {
				// tableName.add(rs.getString("TABLE_NAME"));
				/*System.out.println();
				System.out.println(rsTable.getString("TABLE_NAME"));
				System.out.println("------------------------------");*/

				ResultSet rsColumns = metaData.getColumns(null, null, rsTable.getString("TABLE_NAME"), null);
				while (rsColumns.next()) {
					sqlExec.printResult(rsColumns);
				}
				// i ++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
