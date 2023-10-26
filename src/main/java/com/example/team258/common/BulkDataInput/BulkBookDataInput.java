package com.example.team258.common.BulkDataInput;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class BulkBookDataInput {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/team258?allowLoadLocalInfile=true";
        String user = "root";
        String password = "2028sus300djr";
        String filePath = "F:/hanghae/FinalProjects/bookLibraryCsv.csv"; // 파일 경로

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String query = "LOAD DATA LOCAL INFILE '" + filePath + "' " +
                    "INTO TABLE team258.book_category " +
                    "FIELDS TERMINATED BY ',' " +
                    "ENCLOSED BY '\"' " +
                    "LINES TERMINATED BY '\\n' " +
                    "IGNORE 1 LINES" +
                    "(@a1, @a2)" +
                    "SET book_category_isbn_code = @a1," +
                    "book_category_name = @a2";

            stmt.executeUpdate(query);
            System.out.println("Data imported successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
