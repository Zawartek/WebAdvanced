package ex02;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.FileWriter;
import java.io.IOException;
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

	public ImportDatabase(String database, String url, String driver, String user, String password) {
		this(database);
		initImport(url, driver, user, password);
	}

	public void initImport(String url, String driver, String user, String password) {
		try {
			sqlExec = new SQLExec(url, driver, user, password);
			sqlExec.initialiseConnection();
			dbMetaData = sqlExec.getConnection().getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get all table names and call function that generate the sql code to create them
	 */
	public void importDB() {
		String[] types = { "TABLE" };
		StringBuffer result = new StringBuffer();
		ResultSet tables = null;
		try {
			tables = dbMetaData.getTables(null, null, "%", types);
			if (!tables.next()) {
				System.err.println("Aucune table n'a ete trouve");
			} else {
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
		createScript(result);
		closeResultSet(tables);
	}

	/**
	 * Generate the SQL code to create columns of table
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	private StringBuffer importTable(ResultSet table) throws SQLException {
		StringBuffer result = new StringBuffer();
		ResultSet columns = null;
		String tableName, columnName, columnType, isNullable, nullString;
		int columnSize;
		boolean firstLine = true;
		try {
			tableName = table.getString("TABLE_NAME");
			result.append("\n\n-- " + tableName);
			result.append("\nCREATE TABLE " + tableName + " (\n");

			columns = dbMetaData.getColumns(null, null, table.getString("TABLE_NAME"), null);
			while (columns.next()) {
				if (firstLine) {
					firstLine = false;
				} else {
					result.append(",\n");
				}
				columnName = columns.getString("COLUMN_NAME");
				columnType = columns.getString("TYPE_NAME");
				columnSize = columns.getInt("COLUMN_SIZE");
				isNullable = columns.getString("IS_NULLABLE");
				if ("NO".equalsIgnoreCase(isNullable)) {
					nullString = NOT_NULL_STRING;
				} else {
					nullString = NULL_STRING;
				}
				result.append("\t" + STRING_QUOTE + columnName + STRING_QUOTE + " " + columnType + " (" + columnSize
						+ ")" + " " + nullString);
			}
			closeResultSet(columns);
			result.append(importPrimaryKey(tableName));
			result.append(importForeignKey(tableName));
			result.append(importIndex(tableName));
			result.append("\n);\n");
			// result.append(importTableData(tableName));
		} catch (SQLException e) {
			closeResultSet(columns);
			throw e;
		}
		return result;
	}

	/**
	 * Generate the SQL code to add Primary Keys
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
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
				primaryKeyColumns = new StringBuffer();
				primaryKeyName = pkName;
			}
			if (primaryKeyColumns != null && primaryKeyColumns.length() > 0) {
				primaryKeyColumns.append(", ");
			}
			primaryKeyColumns.append(primaryKeys.getString("COLUMN_NAME"));
		}
		primaryKeyString(result, primaryKeyColumns, primaryKeyName);
		return result;
	}

	private void primaryKeyString(StringBuffer result, StringBuffer primaryKeyColumns, String primaryKeyName) {
		if (primaryKeyColumns != null && primaryKeyColumns.length() > 0) {
			result.append(",\n    PRIMARY KEY ");
			if (primaryKeyName != null) {
				result.append(primaryKeyName);
			}
			result.append(" (" + primaryKeyColumns.toString() + ")");
		}
	}

	/**
	 * Generate the SQL code to add Foreign Keys
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private StringBuffer importForeignKey(String tableName) throws SQLException {
		StringBuffer result = new StringBuffer();
		ResultSet foreignKeys = dbMetaData.getImportedKeys(null, null, tableName);
		String pk_table_name = null, fk_column_name = null, pk_column_name = null;
		while (foreignKeys.next()) {
			fk_column_name = foreignKeys.getString("FKCOLUMN_NAME");
			pk_table_name = foreignKeys.getString("PKTABLE_NAME");
			pk_column_name = foreignKeys.getString("PKCOLUMN_NAME");
			result.append(",\n    FOREIGN KEY " + "(" + fk_column_name + ")");
			result.append(" REFERENCES " + pk_table_name + "(" + pk_column_name + ")");
		}
		return result;
	}

	/**
	 * Generate the SQL code to add Indexes 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private StringBuffer importIndex(String tableName) throws SQLException {
		StringBuffer result = new StringBuffer();
		ResultSet index = dbMetaData.getIndexInfo(null, null, tableName, false, false);
		String indexName = null, idxName = null;
		StringBuffer indexColumns = null;
		boolean non_unique = false, nn_uniq = false;
		while (index.next()) {
			idxName = index.getString("INDEX_NAME");
			nn_uniq = index.getBoolean("NON_UNIQUE");
			if ((idxName != null && !idxName.equals(indexName)) || (indexName != null && !indexName.equals(idxName))) {
				indexString(result, indexColumns, indexName, non_unique);
				indexColumns = new StringBuffer();
				indexName = idxName;
				non_unique = nn_uniq;
			}
			if (indexColumns != null && indexColumns.length() > 0) {
				indexColumns.append(", ");
			}
			indexColumns.append(index.getString("COLUMN_NAME"));
		}
		indexString(result, indexColumns, indexName, non_unique);
		return result;
	}

	private void indexString(StringBuffer result, StringBuffer indexColumns, String indexName, boolean non_unique) {
		if (indexColumns != null && indexColumns.length() > 0 && !indexName.equals("PRIMARY")) {
			if (non_unique) {
				result.append(",\n    KEY ");
			} else {
				result.append(",\n    UNIQUE KEY ");
			}
			if (indexName != null) {
				result.append(indexName);
			}
			result.append(" (" + indexColumns.toString() + ")");
		}
	}
	
	/**
	 * Generate the SQL code to import the data of a table
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private StringBuffer importTableData(String tableName) throws SQLException {
		StringBuffer result = new StringBuffer();
		String request = "SELECT * FROM " + tableName;
		ResultSet data = null;
		ResultSetMetaData metaData;
		int columnCount;
		data = sqlExec.getStatement().executeQuery(request);
		metaData = data.getMetaData();
		columnCount = metaData.getColumnCount();
		result.append("\n\n-- Data for " + tableName + "\n");
		while (data.next()) {
			result.append("INSERT INTO " + tableName + " VALUES (");
			for (int i = 0; i < columnCount; i++) {
				if (i > 0) {
					result.append(", ");
				}
				Object value = data.getObject(i + 1);
				if (value == null) {
					result.append("NULL");
				} else {
					String outputValue = value.toString();
					outputValue = outputValue.replaceAll("'", "\\'");
					result.append("'" + outputValue + "'");
				}
			}
			result.append(");\n");
		}
		return result;
	}

	/**
	 * Close a ResultSet (the result of a query on a database)
	 * @param rs
	 */
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

	/**
	 * Create the script.sql with the string build by requesting the database
	 * @param string
	 */
	private void createScript(StringBuffer string) {
		try {
			FileWriter fichier = new FileWriter("script.sql");
			fichier.write(string.toString());
			fichier.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String database = "sakila";
		ImportDatabase importDB = new ImportDatabase(database);
		importDB.initImport("jdbc:mysql://localhost/sakila", "com.mysql.jdbc.Driver", "root", "root");
		importDB.importDB();
	}
}
