import java.sql.*;

public class Student {
    private static Statement s;
    private static ResultSet r;
    private static Connection conn;

    private enum ORDERING {
        ASC, DESC;
    }

    public static void main(String[] args) throws Exception {

        conn = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=CET", "root", null);
        s = conn.createStatement();
        r = conn.getMetaData().getCatalogs();
        while (r.next()) {
            if (r.getString(1).equals("dbd_student"))
                s.executeUpdate("DROP DATABASE dbd_student;");
        }
        s.executeUpdate("CREATE DATABASE dbd_student CHARACTER SET utf8 COLLATE utf8_unicode_ci;");
        conn = DriverManager.getConnection("jdbc:mysql://localhost/test?serverTimezone=CET", "root", null);
        s = conn.createStatement();
        createRegistrationTable();
        insertRecordsInTable();
        readRecordsFromTable();
    }

    private static void createRegistrationTable() throws SQLException {
        r = conn.getMetaData().getTables(null, "registration", null, null);
        if (r.next())
            return;

        s.executeUpdate("CREATE TABLE registration (id int(11) NOT NULL AUTO_INCREMENT, " +
                "firstname varchar(255) NOT NULL, lastname varchar(255) NOT NULL, age int(11) NOT NULL, " +
                "PRIMARY KEY (id));");


    }

    private static void insertRecordsInTable() throws SQLException {
        s.executeUpdate("INSERT INTO persons (firstname, lastname, age)" +
                " VALUES ('Max', 'Mustermann',  18), ('Katrin', 'Musterfrau', 25), " +
                "('John', 'Doe', 30), ('Becker', 'Heinz', 28);");
    }

    private static void readRecordsFromTable() throws SQLException {
        r = s.executeQuery("SELECT * FROM registration");
        while (r.next()) {
            System.out.println(r.getInt("id"));
        }
    }


    private static void readSortedRecordsFromTable(int columnNr, ORDERING ordering) {
        if (ordering == ORDERING.ASC) {

        }
        if (ordering == ORDERING.DESC) {

        }

    }
}




