package dao;

import core.Database;
import entity.Basket;
import entity.Customer;
import entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BasketDao {

    private Connection connection;
    private ProductDao productDao = new ProductDao();

    public BasketDao(){ this.connection = Database.getInstance();}

    public boolean save(Basket basket){
        String query = "INSERT INTO basket (product_id) VALUES (?)";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1, basket.getProductId());

            return pr.executeUpdate() != -1;
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return true;
    }

    public boolean clear(){
        String query = "DELETE FROM basket";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Basket match(ResultSet rs) throws SQLException {
        Basket basket = new Basket();
        basket.setId(rs.getInt("id"));
        basket.setProductId(rs.getInt("product_id"));
        basket.setProduct(this.productDao.findById(rs.getInt("product_id")));
        return basket;
    }

    public ArrayList<Basket> findAllBaskets(){
        ArrayList<Basket> baskets = new ArrayList<>();
        try {
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM basket");
            while(rs.next()){
                Basket basket = this.match(rs);
                baskets.add(basket);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return baskets;
    }
}
