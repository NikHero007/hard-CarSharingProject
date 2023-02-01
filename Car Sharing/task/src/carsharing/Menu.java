package carsharing;

import org.h2.util.StringUtils;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Menu {
    Statement st;
    CompanyDaoImp companyDaoImp;
    ResultSet rs;

    String logAsManager = "1. Log in as a manager";
    String exit = "0. Exit";
    String companyList = "1. Company list";
    String createCompany = "2. Create a company";
    String back = "0. Back";

    int userInp;

    Scanner sc = new Scanner(System.in);

    public void startMenu() throws  Exception{
        companyDaoImp = new CompanyDaoImp(st);
        System.out.println(logAsManager + "\n" + "2. Log in as a customer" + "\n"
                        + "3. Create a customer" + "\n" + exit);
        userInp = sc.nextInt();

        switch (userInp) {
            case 1 :
                managerMenu();
                break;
            case 2 :
                customerLogin();
                break;
            case 3 :
                createCustomer();
                break;
            case 0 :
                break;
        }
    }

    public void managerMenu() throws Exception{
        System.out.println(companyList + "\n" + createCompany + "\n" + back);
        userInp = sc.nextInt();

        switch (userInp) {
            case 1 :
                String info = companyDaoImp.getCompanyList();
                System.out.print(info);
                if ("The company list is empty!".equals(info)) {
                    managerMenu();
                    break;
                } else {
                    System.out.println("0. Back");
                    userInp = sc.nextInt();
                    if (userInp == 0) {
                        managerMenu();
                    } else {
                        carMenu(userInp);
                    }
                    break;
                }
            case 2 :
                System.out.println("Enter the company name:");
                String companyName = sc.nextLine();
                companyName = sc.nextLine();
                companyDaoImp.updateCompany(companyName);
                managerMenu();
                break;
            case 0 :
                startMenu();
                break;
        }
    }

    public void carMenu(int id) throws Exception {
            Company company = companyDaoImp.getCompany(id);
            String output = "\'" + company.getNAME() + "\' company\n" +
                    "1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back";
            System.out.println(output);

            userInp = sc.nextInt();
            switch (userInp) {
                case 1:
                    String info = companyDaoImp.getCarList(id);
                    System.out.println(info);
                    carMenu(id);
                    break;
                case 2:
                    System.out.println("Enter the car name:");
                    String carName = sc.nextLine();
                    carName = sc.nextLine();
                    companyDaoImp.updateCar(company.getID(), carName);
                    carMenu(id);
                    break;
                case 0:
                    managerMenu();
                    break;
            }
    }

    public void createCustomer() throws Exception {
        System.out.println("Enter the customer name:");
        String coustomerName = sc.nextLine();
        coustomerName = sc.nextLine();
        companyDaoImp.updateCustomer(coustomerName);
        startMenu();
    }
    public void customerLogin() throws Exception {
        String info = companyDaoImp.getCustomerList();
        if ("The customer list is empty!".equals(info)) {
            System.out.println("The customer list is empty!");
            startMenu();
        } else {
            System.out.print(info + "\n");
            userInp = sc.nextInt();
            if (userInp == 0) {
                managerMenu();
            } else {
                customerMenu(userInp);
            }
        }
    }

    public void customerMenu(int id) throws Exception {
        Customer customer = companyDaoImp.getCustomer(id);
        Integer RENTED_CAR_ID = null;
        String output = "1. Rent a car\n" +
                        "2. Return a rented car\n" +
                        "3. My rented car\n" +
                        "0. Back";
        System.out.println(output);

        userInp = sc.nextInt();
        switch (userInp) {
            case 1:
                String companyList = companyDaoImp.getCompanyList();
                RENTED_CAR_ID = customer.getRENTED_CAR_ID();
                if (RENTED_CAR_ID != 0) {
                    System.out.println("You've already rented a car!");
                    customerMenu(id);
                } else if ("The company list is empty!".equals(companyList)) {
                    System.out.print(companyList);
                    customerMenu(id);
                } else {
                    chooseCar(id);
                }
                    break;
             case 2:
                RENTED_CAR_ID = customer.getRENTED_CAR_ID();
                if (RENTED_CAR_ID == 0) {
                    System.out.println("You didn't rent a car!");
                } else {
                    String NAME = customer.getNAME();
                    String sql = "UPDATE CUSTOMER " +
                                "SET RENTED_CAR_ID = null " +
                                "WHERE NAME LIKE ('" + NAME + "')";
                    st.executeUpdate(sql);
                    System.out.println("You've returned a rented car!");
                }
                customerMenu(id);
                break;
            case 3:
                String NAME = "";
                String COMPANY_NAME = "";
                int COMPANY_ID = 0;
                RENTED_CAR_ID = customer.getRENTED_CAR_ID();
                if (RENTED_CAR_ID == 0) {
                    System.out.println("You didn't rent a car!");
                    customerMenu(id);
                    break;
                } else {
                    String sql = "SELECT NAME, COMPANY_ID FROM CAR WHERE ID = "  + RENTED_CAR_ID;
                    rs = st.executeQuery(sql);
                    while(rs.next()) {
                        NAME = rs.getString("NAME");
                        COMPANY_ID = rs.getInt("COMPANY_ID");
                    }

                    sql = "SELECT NAME FROM COMPANY WHERE ID = "  + COMPANY_ID;
                    rs = st.executeQuery(sql);
                    while(rs.next()) {
                        COMPANY_NAME = rs.getString("NAME");
                    }
                    String outp = "Your rented car:\n" +
                            NAME + "\n" +
                            "Company:\n" +
                            COMPANY_NAME;
                    System.out.println(outp);
                    customerMenu(id);
                    break;
                }
            case 0:
                startMenu();
                break;
        }
    }

    public void chooseCar(int customerID) throws Exception {
        int id = customerID;
        String chooseCareMenu = "Choose a car:\n";
        int count = -1;
        System.out.println(companyDaoImp.getCompanyList() + "0. Back\n");
        int userInp = sc.nextInt();
        Company company = companyDaoImp.getCompany(userInp);


        String sql = "SELECT count(car.ID) AS count " +
                    "FROM CAR LEFT JOIN CUSTOMER ON CAR.id = CUSTOMER.rented_car_id " +
                    "WHERE company_id = " + userInp +
                    " AND CUSTOMER.rented_car_id is null";
        rs = st.executeQuery(sql);
        while (rs.next()) {
            count = rs.getInt("count");
        }


        if(count == 0) {
            System.out.println("No available cars in the " + company.getNAME() + " company");
            customerMenu(id);
        } else {
            String[] carList = new String[count];

            sql = "SELECT CAR.id as ID, CAR.name as NAME, CAR.company_id as COMPANY_ID " +
                    "FROM CAR LEFT JOIN CUSTOMER ON CAR.id = CUSTOMER.rented_car_id " +
                    "WHERE company_id = " + userInp +
                    " AND CUSTOMER.rented_car_id is null";
            rs = st.executeQuery(sql);
            int counter = 1;
            while (rs.next()) {
                String name = rs.getString("NAME");
                chooseCareMenu += counter + ". " + name + "\n";
                carList[counter - 1] = name;
                counter++;
            }
            chooseCareMenu += "0. Back";
            System.out.println(chooseCareMenu);

            // тут рентим именно нужную машину ХЗ КАК СДЕЛАТЬ
            int userInp2 = sc.nextInt();
            if (userInp2 == 0) {
                customerMenu(id);
            } else {
                int rentedCarID = -1;
                int rentedCarCompanyID = -1;

                String choosenCar = carList[userInp2 - 1];
                sql = "SELECT ID, COMPANY_ID " +
                        "FROM CAR " +
                        "WHERE name = '" + choosenCar + "'";
                rs = st.executeQuery(sql);

                while (rs.next()) {
                    rentedCarID = rs.getInt("ID");
                    rentedCarCompanyID = rs.getInt("COMPANY_ID");
                }
                System.out.println("You rented '" + choosenCar + "'");

                sql = "UPDATE CUSTOMER " +
                        "SET RENTED_CAR_ID = " + rentedCarID +
                        "WHERE ID = " + id;
                st.executeUpdate(sql);
                customerMenu(id);
            }
        }
    }





    public Menu(Statement st) {
            this.st = st;
        }
}
