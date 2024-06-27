package business;

import core.Helper;
import core.Item;
import dao.ProductDao;
import entity.Product;

import java.util.ArrayList;

public class ProductController {

    private final ProductDao productDao = new ProductDao();

    public ArrayList<Product> findAllProducts(){
        return this.productDao.findAllProduct();
    }

    public Product findById(int id){
        return this.productDao.findById(id);
    }

    public boolean save(Product product){
        return productDao.save(product);
    }

    public boolean update(Product product){
        if(this.productDao.findById(product.getId()) == null){
            Helper.showMessage(product.getId() + " id numaralı ürün bulunamadı!");
            return false;
        }
        else{
            return productDao.update(product);
        }
    }

    public boolean delete(int id){
        return this.productDao.delete(id);
    }

    public ArrayList<Product> filter(String name, String code, Item isStock){
        String query = "SELECT * FROM product";

        ArrayList<String> whereList = new ArrayList<>();

        if(name.length() > 0){
            whereList.add("name LIKE '%" + name + "%'");
        }
        if(code.length() > 0){
            whereList.add("code LIKE '%" + code + "%'");
        }

        if(isStock != null){
            if(isStock.getKey() == 1){
                whereList.add("stock > 0");
            }else{
                whereList.add("stock <= 0");
            }
        }

        if(whereList.size() > 0){
            String whereQuery = String.join(" AND ", whereList);
            query += " WHERE " + whereQuery;
        }

        return  this.productDao.query(query);
    }
}
