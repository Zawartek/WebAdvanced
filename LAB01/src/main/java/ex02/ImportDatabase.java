package ex02;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;

public class ImportDatabase {
	private static final String NULL_STRING = "NULL";
	private static final String NOT_NULL_STRING = "NOT NULL";
	private static final String STRING_QUOTE = "\'";
	
	private String database;
	private SQLExec sqlExec;
	private DatabaseMetaData dbMetaData;

	public ImportDatabase(String database) {
		this.database = database;
	}
	
	public ImportDatabase(String database,String url, String driver, String user, String password) {
		this(database);
		initImport(url, driver, user, password);
	}
	
	public void initImport(String url, String driver, String user, String password) {
		try {
			sqlExec = new SQLExec(url, driver, user, password);
			sqlExec.initialiseConnection();
			dbMetaData=sqlExec.getConnection().getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void importDB() {
		String[] types = {"TABLE"};
		StringBuffer result = new StringBuffer();
		ResultSet tables = null;
		try {
			tables=dbMetaData.getTables(null,null,"%",types);
			if (!tables.next()) {
                System.err.println("Aucune table n'a ete trouve");
			}
			else {
				do {
					result.append(importTable(tables));
				} while (tables.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlExec.close();
		}
		System.out.println(result);
		closeResultSet(tables);
	}
	
	private StringBuffer importTable(ResultSet table) throws SQLException {
		StringBuffer result = new StringBuffer();
		ResultSet columns = null;
		String tableName, columnName, columnType, isNullable, nullString;
		int columnSize;
		boolean firstLine = true;
		try {
			tableName = table.getString("TABLE_NAME");
			result.append("\n\n-- "+tableName);
            result.append("\nCREATE TABLE "+tableName+" (\n");
            
			columns = dbMetaData.getColumns(null, null, table.getString("TABLE_NAME"), null);
			while (columns.next()) {
	            if (firstLine) {
	                firstLine = false;
	            } else {
	                result.append(",\n");
	            }
	            columnName = columns.getString("COLUMN_NAME");
	            columnType = columns.getString("TYPE_NAME");
	            // WARNING: this may give daft answers for some types on some databases (eg JDBC-ODBC link)
	            columnSize = columns.getInt("COLUMN_SIZE");
	            isNullable = columns.getString("IS_NULLABLE");
	            if ("NO".equalsIgnoreCase(isNullable)) {
	            	nullString = NOT_NULL_STRING;
	            }
	            else {
	            	nullString = NULL_STRING;
	            }
	            result.append("\t"+STRING_QUOTE+columnName+STRING_QUOTE+" "+columnType+" ("+columnSize+")"+" "+nullString);
	        }
			closeResultSet(columns);
			result.append(importPrimaryKey(tableName));
            result.append("\n);\n");
            result.append(importTableData(tableName));
		}
		catch(SQLException e) {
			closeResultSet(columns);
			throw e;
		}
		return result;
	}
	

	private StringBuffer importPrimaryKey(String tableName) throws SQLException {
		StringBuffer result = new StringBuffer();
        ResultSet primaryKeys = dbMetaData.getPrimaryKeys(null, null, tableName);
        String primaryKeyName = null;
        StringBuffer primaryKeyColumns = null;
        while (primaryKeys.next()) {
            String pkName = primaryKeys.getString("PK_NAME");
            if ((pkName != null && !pkName.equals(primaryKeyName))
                    || (primaryKeyName != null && !primaryKeyName.equals(pkName))) {
                primaryKeyString(result, primaryKeyColumns, primaryKeyName);
                // Start again with the new name
                primaryKeyColumns = new StringBuffer();
                primaryKeyName = pkName;
            }
            // Now append the column
            if (primaryKeyColumns!=null
            		&& primaryKeyColumns.length() > 0) {
                primaryKeyColumns.append(", ");
            }
            primaryKeyColumns.append(primaryKeys.getString("COLUMN_NAME"));
        }
        primaryKeyString(result, primaryKeyColumns, primaryKeyName);
        return result;
	}
	
	private void primaryKeyString(StringBuffer result, StringBuffer primaryKeyColumns, String primaryKeyName) {
		if (primaryKeyColumns!=null
        		&& primaryKeyColumns.length() > 0) {
            // There's something to output
            result.append(",\n    PRIMARY KEY ");
            if (primaryKeyName != null) { result.append(primaryKeyName); }
            result.append(" ("+primaryKeyColumns.toString()+")");
        }
	}

	private StringBuffer importTableData(String tableName) throws SQLException {
		StringBuffer result = new StringBuffer();
		String request ="SELECT * FROM " + tableName;
		ResultSet data = null;
        ResultSetMetaData metaData;
        int columnCount;
        data = sqlExec.getStatement().executeQuery(request);
		metaData = data.getMetaData();
		columnCount = metaData.getColumnCount();
		result.append("\n\n-- Data for "+tableName+"\n");
        while (data.next()) {
            result.append("INSERT INTO "+tableName+" VALUES (");
            for (int i=0; i<columnCount; i++) {
                if (i > 0) {
                    result.append(", ");
                }
                Object value = data.getObject(i+1);
                if (value == null) {
                    result.append("NULL");
                } else {
                    String outputValue = value.toString();
                    outputValue = outputValue.replaceAll("'","\\'");
                    result.append("'"+outputValue+"'");
                }
            }
            result.append(");\n");
        }
        sqlExec.close();
		return result;
	}
	
	private void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
		}
	}
	
	public static void main (String[] args) {
		String database = "sakila";
		ImportDatabase importDB = new ImportDatabase(database);
		importDB.initImport("jdbc:mysql://localhost/sakila", "com.mysql.jdbc.Driver", "root", "root");
		importDB.importDB();
		
		
	}
}
