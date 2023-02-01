package carsharing;

import java.sql.ResultSet;
import java.sql.Statement;

public class CompanyDaoImp implements CompanyDao{
    Statement st;
    ResultSet rs;
    int count;
    String sql;


    @Override
    public String getCompanyList() throws Exception {
        String output = "Choose the company:\n";

        sql = "SELECT count(ID) AS count FROM COMPANY";
        rs = st.executeQuery(sql);
        while (rs.next()) {
            count = rs.getInt("count");
        }

        if(count == 0) {
            return "The company list is empty!";
        } else {
            sql = "SELECT ID, NAME FROM COMPANY";
            rs = st.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                output += id + ". " + name + "\n";
            }
            return output;
        }
    }

    @Override
    public String getCarList(int companyID) throws Exception {
        String info = "Car list:\n";
        sql = "SELECT count(ID) AS count FROM CAR WHERE COMPANY_ID = " + companyID;
        rs = st.executeQuery(sql);
        while (rs.next()) {
            count = rs.getInt("count");
        }

        if(count == 0) {
            return "The car list is empty!";
        } else {
            sql = "SELECT NAME FROM CAR WHERE COMPANY_ID = " + companyID;
            rs = st.executeQuery(sql);
            int counter = 1;
            while (rs.next()) {
                String name = rs.getString("NAME");
                info += counter + ". " + name + "\n";
                counter++;
            }

            return info;
        }
    }

    @Override
    public String getCustomerList() throws Exception {
        String output = "Choose a customer:\n";

        sql = "SELECT count(ID) AS count FROM CUSTOMER";
        rs = st.executeQuery(sql);
        while (rs.next()) {
            count = rs.getInt("count");
        }

        if(count == 0) {
            return "The customer list is empty!";
        } else {
            sql = "SELECT ID, NAME FROM CUSTOMER";
            rs = st.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                output += id + ". " + name + "\n";
            }
            output += "0. Back" + "\n";
            return output;
        }
    }


    @Override
    public Company getCompany(int id) throws Exception {
        int ID = 0;
        String NAME = null;
        sql = "SELECT * FROM COMPANY " +
                "WHERE ID = " + id;

        rs = st.executeQuery(sql);
        while (rs.next()) {
            ID = rs.getInt("ID");
            NAME = rs.getString("NAME");
        }
        Company company = new Company(ID, NAME);

        return company;
    }

    @Override
    public Customer getCustomer(int id) throws Exception {
        int ID = id;
        String NAME = null;
        Integer RENTED_CAR_ID = null;
        Customer customer = null;
        sql = "SELECT * FROM CUSTOMER " +
                "WHERE ID = " + id;

        rs = st.executeQuery(sql);
        while (rs.next()) {
            ID = rs.getInt("ID");
            NAME = rs.getString("NAME");
            RENTED_CAR_ID = rs.getInt("RENTED_CAR_ID");
            if(RENTED_CAR_ID == null) {
                customer = new Customer(ID, NAME);
            } else {
                customer = new Customer(ID, NAME, RENTED_CAR_ID);
            }
        }

        return customer;
    }

    @Override
    public void updateCompany(String name) throws Exception {
        sql = "INSERT INTO COMPANY (NAME) VALUES ('" + name + "')";
        st.executeUpdate(sql);

        System.out.println("The company was created!");
    }

    @Override
    public void updateCar(int id, String name) throws Exception {
        sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('" + name + "', " + "'" + id + "')";
        st.executeUpdate(sql);

        System.out.println("The car was added!");
    }

    @Override
    public void updateCustomer(String name) throws Exception {
        sql = "INSERT INTO CUSTOMER (NAME) VALUES ('" + name + "')";
        st.executeUpdate(sql);

        System.out.println("The customer was added!");
    }

    @Override
    public void deleteCompany() {

    }

    public CompanyDaoImp(Statement st) {
        this.st = st;
    }
}
