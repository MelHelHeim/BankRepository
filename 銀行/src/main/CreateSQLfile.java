package main;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CreateSQLfile {
	public static void setup() {
		String baseUrl = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=Asia/Tokyo";
		String dbUrl = "jdbc:mysql://localhost:3306/ATMDatabase?useSSL=false&serverTimezone=Asia/Tokyo";
		String user = "root";
		String password = "Sadaharuda4!";
		boolean shouldRunSetup = false;

		try (Connection baseCon = DriverManager.getConnection(baseUrl, user, password);
				ResultSet dbs = baseCon.getMetaData().getCatalogs()) {

			boolean dbExists = false;
			while (dbs.next()) {
				if ("ATMDatabase".equalsIgnoreCase(dbs.getString(1))) {
					dbExists = true;
					break;
				}
			}
			if (!dbExists) {
				shouldRunSetup = true;
			} else {
				try (Connection dbCon = DriverManager.getConnection(dbUrl, user, password);
						ResultSet tables = dbCon.getMetaData().getTables(null, null, "users", null)) {
					
					if (!tables.next()) {
						shouldRunSetup = true;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if (!shouldRunSetup)
			return;

		try {
			String sql = new String(Files.readAllBytes(Paths.get("src/resources/setup.sql")));
			String[] sqlStatements = sql.split(";");

			try (Connection setUpCon = DriverManager.getConnection(baseUrl, user, password);
					Statement stmt = setUpCon.createStatement()) {

				for (String statement : sqlStatements) {
					statement = statement.trim();
					if (!statement.isEmpty()) {
						stmt.execute(statement);
						System.out.println("Executed: " + statement);
					}
				}
				System.out.println("exected Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
