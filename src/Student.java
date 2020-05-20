/*
 * Creator: David Riegler (20.05.2020)
 * Last changed by: David Riegler
 * Last change: 20.05.2020
 */

import java.sql.*;

public class Student {
    private static Statement s;
    private static ResultSet r;

    private enum ORDERING {
        ASC, DESC;
    }

    public static void main(String[] args) throws Exception {
        boolean exists = false;
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=CET", "root", null);
        s = conn.createStatement();
        r = conn.getMetaData().getCatalogs();
        while (r.next()) {
            if (r.getString(1).equals("dbd_student")) {
                System.out.println("Database already exists");
                exists = true;
            }
        }

        if (!exists) {
            s.executeUpdate("CREATE DATABASE dbd_student CHARACTER SET utf8 COLLATE utf8_unicode_ci;");
        }

        conn = DriverManager.getConnection("jdbc:mysql://localhost/dbd_student?serverTimezone=CET", "root", null);
        s = conn.createStatement();
        s.executeUpdate("DROP TABLE registration");
        r = conn.getMetaData().getTables(null, null, "registration", null);
        if (r.next()) {
            System.out.println("Table already exists");
        } else {
            createRegistrationTable();
            insertRecordsInTable();
        }
        readRecordsFromTable();
        readSortedRecordsFromTable(2, ORDERING.DESC);
    }

    private static void createRegistrationTable() throws SQLException {
        s.executeUpdate("CREATE TABLE registration (id int(11) NOT NULL AUTO_INCREMENT, " +
                "firstname varchar(255) NOT NULL, lastname varchar(255) NOT NULL, age int(11) NOT NULL, " +
                "PRIMARY KEY (id));");
    }

    private static void insertRecordsInTable() throws SQLException {
        s.executeUpdate("INSERT INTO registration (firstname, lastname, age)" +
                " VALUES ('Max', 'Mustermann',  18), ('Katrin', 'Musterfrau', 25), " +
                "('John', 'Doe', 30), ('Becker', 'Heinz', 28);");
    }

    private static void readRecordsFromTable() throws SQLException {
        r = s.executeQuery("SELECT * FROM registration");
        while (r.next()) {
            System.out.println("Age:" + r.getInt("age") + ", First name:" + r.getString("firstname") +
                    ", Last name:" + r.getString("lastname"));
        }
    }

    private static void readSortedRecordsFromTable(int columnNr, ORDERING ordering) throws SQLException {
        String nameOfColumn;
        if (columnNr == 0)
            nameOfColumn = "id";
        else if (columnNr == 1)
            nameOfColumn = "firstname";
        else if (columnNr == 2)
            nameOfColumn = "lastname";
        else if (columnNr == 3)
            nameOfColumn = "age";
        else {
            System.out.println("Column number " + columnNr + " doesn't exist");
            return;
        }
        System.out.println("[ID,First name, Last name, Age]");
        r = s.executeQuery("SELECT * FROM registration ORDER BY " + nameOfColumn + " " + ordering);
        while (r.next()) {
            System.out.println(r.getInt("id") - 1 + ", " + r.getString("firstname") + ", "
                    + r.getString("lastname") + ", " + r.getString("age"));
        }
    }
}