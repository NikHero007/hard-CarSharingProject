package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        // получаем имя ДБ
        String dbName = "";
        for(int x = 0; x < args.length; x++) {
            if(args[x].equals("-databaseFileName")) {
                dbName = (x + 1) == args.length ? "notDefined" : args[x+1];
            }
        }


        try {
            Class.forName("org.h2.Driver");

        Connection conn = DriverManager.getConnection
                ("jdbc:h2:file:../task/src/carsharing/db/" + dbName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();

        // создаем ДБ с компаниями
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS COMPANY " +
                "(ID INT PRIMARY KEY AUTO_INCREMENT, " +
                " NAME VARCHAR NOT NULL UNIQUE )";
        st.execute(sqlCreateTable);

        String sqlDelete = "ALTER TABLE COMPANY ALTER COLUMN ID RESTART WITH 1";
        st.execute(sqlDelete);

        // создаем ДБ с машинами
        String sqlCreateTable2 = "CREATE TABLE IF NOT EXISTS CAR " +
                "(ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR NOT NULL UNIQUE, " +
                "COMPANY_ID INT NOT NULL, " +
                "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID))";
        st.execute(sqlCreateTable2);

        // создаем ДБ с ользователями
        String sqlCreateTable3 = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                "(ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR NOT NULL UNIQUE, " +
                "RENTED_CAR_ID INT, " +
                "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR (ID))";
        st.execute(sqlCreateTable3);

        // выполнение программы

        Menu menu = new Menu(st);
        menu.startMenu();

        conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }







    }
}