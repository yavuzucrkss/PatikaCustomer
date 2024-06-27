package view;

import business.ProductController;
import core.Helper;
import entity.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductUI extends JFrame{
    private JPanel container;
    private JLabel lbl_title;
    private JTextField fld_product_name;
    private JTextField fld_product_code;
    private JTextField fld_product_price;
    private JTextField fld_product_stock;
    private JButton btn_save;
    private JLabel lbl_product_name;
    private JLabel lbl_product_code;
    private JLabel lbl_product_price;
    private JLabel lbl_product_stock;
    private Product product;
    private ProductController productController;

    public ProductUI(Product product){
        this.product = product;
        this.productController = new ProductController();

        this.add(container);
        this.setTitle("Ürün Ekle/Düzenle");
        this.setSize(300,500);
        this.setVisible(true);
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height) / 2;
        this.setLocation(x, y);


        if(this.product == null){
            this.lbl_title.setText("Ürün Ekle");
        }else{
            this.lbl_title.setText("Ürünü Güncelle ");
            this.fld_product_name.setText(this.product.getName());
            this.fld_product_code.setText(this.product.getCode());
            this.fld_product_price.setText(String.valueOf(this.product.getPrice()));
            this.fld_product_stock.setText(String.valueOf(this.product.getStock()));
        }
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField[] textFields = {fld_product_name,fld_product_code,fld_product_price,fld_product_stock};
                if(Helper.isFieldListEmpty(textFields)){
                    Helper.showMessage("fill");
                }
                else if(!Helper.isNumber(fld_product_price.getText()) || !Helper.isNumber(fld_product_stock.getText())){
                    Helper.showMessage("Fiyat ve Stock Satırları Sayı Olmalıdır.");
                }
                else{
                    boolean result = false;
                    product.setName(fld_product_name.getText());
                    product.setCode(fld_product_code.getText());
                    product.setPrice(Integer.parseInt(fld_product_price.getText()));
                    product.setStock(Integer.parseInt(fld_product_stock.getText()));

                    if(product.getId() == 0){
                        result = productController.save(product);
                    }else{
                        result = productController.update(product);
                    }

                    if(result){
                        Helper.showMessage("done");
                        dispose();
                    }else{
                        Helper.showMessage("error");
                    }
                }
            }
        });
    }

}
