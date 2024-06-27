package dao;

import business.CustomerController;
import business.ProductController;
import core.Database;
import entity.Basket;
import entity.Cart;
import entity.Customer;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CartDao {

    private Connection connection;
    private CustomerController customerController = new CustomerController();
    private ProductController productController = new ProductController();

    public CartDao() { this.connection = Database.getInstance(); }

    public ArrayList<Cart> findAll(){
        ArrayList<Cart> carts = new ArrayList<>();
        try {
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM cart");
            while(rs.next()){
                Cart basket = this.match(rs);
                carts.add(basket);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return carts;
    }

    public boolean save(Cart cart){
        String query = "INSERT INTO cart (customer_id,product_id,price,date,note) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1,cart.getCustomeId());
            pr.setInt(2,cart.getProductId());
            pr.setInt(3,cart.getPrice());
            pr.setDate(4, Date.valueOf(cart.getDate()));
            pr.setString(5,cart.getNote());

            return pr.executeUpdate() != -1;
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return true;
    }

    public Cart match(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setId(rs.getInt("id"));
        cart.setPrice(rs.getInt("price"));
        cart.setCustomeId(rs.getInt("customer_id"));
        cart.setProductId(rs.getInt("product_id"));
        cart.setNote(rs.getString("note"));
        cart.setDate(LocalDate.parse(rs.getString("date")));
        cart.setCustomer(this.customerController.findById(rs.getInt("customer_id")));
        cart.setProduct(this.productController.findById(rs.getInt("product_id")));
        return cart;
    }
}
