package view;

import business.BasketController;
import business.CartController;
import business.ProductController;
import core.Helper;
import entity.Basket;
import entity.Cart;
import entity.Customer;
import entity.Product;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CartUI  extends  JFrame{
    private JPanel container;
    private JLabel lbl_title;
    private JTextField fld_cart_date;
    private JTextArea textArea1tarea_cart_note;
    private JButton btn_cart_create;
    private JLabel lbl_customer_Name;
    private Customer customer;
    private BasketController basketController;
    private CartController cartController;
    private ProductController productController;
    public CartUI(Customer customer){
        this.customer = customer;
        this.basketController = new BasketController();
        this.cartController = new CartController();
        this.productController = new ProductController();


        this.add(container);
        this.setTitle("Sipariş Oluştur");
        this.setSize(300,500);
        this.setVisible(true);
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height) / 2;
        this.setLocation(x, y);

        if(customer.getId() == 0){
            Helper.showMessage("Lütfen geçerli bir müşteri seçiniz.");
            dispose();
        }

        ArrayList<Basket> baskets = this.basketController.findALlBaskets();
        if(baskets.size() == 0){
            Helper.showMessage("Lütfen sepete ürün ekleyiniz!");
            dispose();
        }

        this.lbl_customer_Name.setText("Müşteri: " + customer.getName());
        btn_cart_create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btn_cart_create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Helper.isFieldEmpty(fld_cart_date)){
                    Helper.showMessage("fill");
                }else{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    for(Basket basket : baskets){
                        if(basket.getProduct().getStock() <= 0){
                            continue;
                        }
                        Cart cart = new Cart();
                        cart.setCustomeId(customer.getId());
                        cart.setProductId(basket.getId());
                        cart.setPrice(basket.getProduct().getPrice());
                        cart.setDate(LocalDate.parse(fld_cart_date.getText(),formatter));
                        cart.setNote(textArea1tarea_cart_note.getText());
                        cartController.save(cart);

                        Product unStockProduct = basket.getProduct();
                        unStockProduct.setStock(unStockProduct.getStock() - 1);

                        productController.update(unStockProduct);
                    }
                    basketController.clear();
                    Helper.showMessage("done");
                    dispose();
                }
            }
        });
    }

    private void createUIComponents() throws ParseException {
        this.fld_cart_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fld_cart_date.setText(formatter.format(LocalDate.now()));
    }
}
