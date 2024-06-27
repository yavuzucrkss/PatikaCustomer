package business;

import core.Helper;
import dao.CustomerDao;
import entity.Customer;

import java.util.ArrayList;

public class CustomerController {
    private final CustomerDao customerDao = new CustomerDao();

    public ArrayList<Customer> findAll() {
        return this.customerDao.findAllCustomers();
    }

    public boolean save(Customer customer){
        return customerDao.save(customer);
    }

    public boolean update(Customer customer){
        if(this.findById(customer.getId()) == null){
            Helper.showMessage("Bu id'ye ait müşteri Bulunamadı");
            return false;
        }
        return customerDao.update(customer);
    }

    public Customer findById(int id){
        return customerDao.findByID(id);
    }

    public boolean delete(int id){
        if(this.findById(id) == null){
            Helper.showMessage(id + " numaralı müşteri bulunamadı");
            return false;
        }
        return customerDao.delete(id);
    }

    public ArrayList<Customer> filter(String name, Customer.TYPE type){
        String query = "SELECT * FROM customer";

        ArrayList<String> whereList = new ArrayList<>();

        if(name.length() > 0){
            whereList.add("name LIKE '%" + name + "%'");
        }

        if(type != null){
            whereList.add("type ='" + type + "'");
        }

        if(whereList.size() > 0){
            String whereQuery = String.join(" AND ", whereList);
            query += " WHERE " + whereQuery;
        }

        return this.customerDao.query(query);
    }
}
