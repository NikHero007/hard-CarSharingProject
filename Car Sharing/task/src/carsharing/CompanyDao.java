package carsharing;

public interface CompanyDao {
    public String getCompanyList() throws Exception;
    public String getCarList(int id) throws Exception;
    public String getCustomerList() throws Exception;
    public Company getCompany(int id) throws Exception;
    public Customer getCustomer(int id) throws Exception;
    public void updateCompany(String name) throws Exception;
    public void updateCar(int id, String name) throws Exception;
    public void updateCustomer(String name) throws Exception;
    public void deleteCompany();
}
