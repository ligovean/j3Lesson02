import java.sql.*;

public class Main {
    private static Connection connection;
    private static Statement stmnt;
    private static PreparedStatement prpStmnt;



    public static void main(String[] args) {

        try {
            connect();

            //Создание Таблицы CREATE
           int res = stmnt.executeUpdate("CREATE TABLE students (id INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, name  TEXT, score TEXT);");

            //Наполнение Таблицы INSERT
            prpStmnt = connection.prepareStatement("INSERT INTO students(Name, score) VALUES(?, ?);");

            connection.setAutoCommit(false);

            for (int i = 1; i <= 5000; i++) {
                prpStmnt.setString(1, "Ivan " + i);
                prpStmnt.setString(2, i+"");
                prpStmnt.addBatch();
            }
            prpStmnt.executeBatch();

            connection.setAutoCommit(true);

            //Запрос по нескольки записям SELECT
            ResultSet rs = stmnt.executeQuery("SELECT * FROM students WHERE name LIKE '%333%'");
            while (rs.next()){
                System.out.println(rs.getString(2));
            }

            //Удаление нескольких записей из таблии DELETE
            res = stmnt.executeUpdate("DELETE FROM students WHERE name LIKE '%555%'");


            //Обновление нескольких записей UPDATE
            res = stmnt.executeUpdate("UPDATE students SET score = '0' WHERE name LIKE '%3%'");

            //Удаление таблицы DROP
            res = stmnt.executeUpdate("DROP TABLE students");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void connect() throws ClassNotFoundException,SQLException {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:students.db");
            stmnt = connection.createStatement();
    }

    public static void disconnect() throws SQLException{
            connection.close();
    }
}
