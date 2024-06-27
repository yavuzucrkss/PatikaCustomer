package view;

import business.BasketController;
import business.CartController;
import business.CustomerController;
import business.ProductController;
import core.Helper;
import core.Item;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DashboardUI extends JFrame {
    private JPanel container;
    private JPanel pnl_top;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JTabbedPane pnl_product;
    private JPanel pnl_customer;
    private JScrollPane scrl_customer;
    private JTable tbl_customer;
    private JPanel pnl_customer_filter;
    private JTextField fld_f_customername;
    private JComboBox<Customer.TYPE> cmb_customertype;
    private JButton btn_customerfilter;
    private JButton btn_customer_reset;
    private JButton btn_customer_new;
    private JLabel lbl_f_customername;
    private JLabel lbl_f_customertype;
    private JScrollPane scrl_product;
    private JTable tbl_product;
    private JPanel pnl_product_filet;
    private JTextField fld_f_product_name;
    private JTextField fld_f_product_code;
    private JComboBox<Item> cmb_f_stock_status;
    private JButton btn_product_filter;
    private JButton btn_product_reset;
    private JButton btn_product_new;
    private JLabel lbl_f_productname;
    private JLabel lbl_f_product_code;
    private JLabel lbl_f_stock_status;
    private JPanel pnl_basket;
    private JPanel pnl_basket_top;
    private JScrollPane scrl_basket;
    private JComboBox cmb_basket_customer;
    private JLabel lbl_customer_select;
    private JLabel lbl_basket_count;
    private JLabel lbl_basket_price;
    private JButton btn_basket_reset;
    private JButton btn_basket_new;
    private JTable tbl_basket;
    private JPanel pnl;
    private JScrollPane scrl_Cart;
    private JTable tbl_cart;
    private User user;
    private CustomerController customerController;
    private ProductController productController;
    private BasketController basketController;
    private CartController cartController;
    private DefaultTableModel tmdl_customer = new DefaultTableModel();
    private DefaultTableModel tmdl_product = new DefaultTableModel();
    private DefaultTableModel tmdl_basket = new DefaultTableModel();
    private DefaultTableModel tmdl_cart = new DefaultTableModel();
    private JPopupMenu popup_customer = new JPopupMenu();
    private JPopupMenu popup_product = new JPopupMenu();


    public DashboardUI(User user) {
        this.user = user;
        this.customerController = new CustomerController();
        this.productController = new ProductController();
        this.basketController = new BasketController();
        this.cartController = new CartController();

        if (user == null) {
            Helper.showMessage("error");
            dispose();
        }

        this.add(container);
        this.setTitle("Müşteri Yönetim Sistemi");
        this.setSize(1000, 500);
        this.setVisible(true);
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height) / 2;
        this.setLocation(x, y);

        this.lbl_welcome.setText("Hoşgeldin " + this.user.getName());


        btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginUI loginUI = new LoginUI();
            }
        });

        //CUSTOMER TAB
        loadCustomerTable(null);
        loadCustomerPopupMenu();
        loadCustomerButtonEvent();
        this.cmb_customertype.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));
        this.cmb_customertype.setSelectedItem(null);

        //PRODUCT TAB
        loadProductTable(null);
        loadProductPopupMenu();
        loadProductButtonEvent();
        this.cmb_f_stock_status.addItem(new Item(1,"Stockta Var"));
        this.cmb_f_stock_status.addItem(new Item(2,"Stokta Yok"));
        this.cmb_f_stock_status.setSelectedItem(null);

        //BASKET TAB
        loadBasketTable();
        loadBasketButtonEvent();
        loadBasketCustomerCombo();
        
        //CART TAB
        loadCartTable();

    }

    private void loadCartTable() {
        Object[] columnCart = {"ID", "Müşteri Adı", "Ürün Adı", "Fiyatı", "Tarihi", "Not"};
        ArrayList<Cart> carts = this.cartController.findAll();

        DefaultTableModel cleaeModel = (DefaultTableModel) this.tbl_cart.getModel();
        cleaeModel.setRowCount(0);

        this.tmdl_cart.setColumnIdentifiers(columnCart);
        for (Cart cart : carts) {
            Object[] tableObject = {
                    cart.getId(),
                    cart.getCustomer().getName(),
                    cart.getProduct().getName(),
                    cart.getPrice(),
                    cart.getDate(),
                    cart.getNote()
            };
            this.tmdl_cart.addRow(tableObject);
        }

        this.tbl_cart.setModel(tmdl_cart);
        this.tbl_cart.getTableHeader().setReorderingAllowed(false);
        this.tbl_cart.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_cart.setEnabled(false);
    }

    private void loadBasketCustomerCombo() {
        ArrayList<Customer> customers = this.customerController.findAll();
        this.cmb_basket_customer.removeAllItems();
        for(Customer customer : customers){
             int comboKey = customer.getId();
             String comboValue = customer.getName();

             cmb_basket_customer.addItem(new Item(comboKey,comboValue));
        }
        this.cmb_basket_customer.setSelectedItem(null);
    }

    private void loadBasketButtonEvent() {
        btn_basket_reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(basketController.clear()){
                    Helper.showMessage("done");
                    loadBasketTable();
                }
                else{
                    Helper.showMessage("error");
                }

            }
        });
        
        btn_basket_new.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Item selectedCustomer = (Item) cmb_basket_customer.getSelectedItem();
                if(selectedCustomer == null){
                    Helper.showMessage("Lütfen bir müşteri seçiniz");
                }else{
                    Customer customer = customerController.findById(selectedCustomer.getKey());
                    ArrayList<Basket> baskets = basketController.findALlBaskets();
                    if(customer.getId() == 0){
                        Helper.showMessage("Böyle bir müşteri bulunamadı");
                    }
                    else if(baskets.size() == 0){
                        Helper.showMessage("Lütfen sepete bir ürün ekleyiniz.");
                    }else{
                        CartUI cartUI = new CartUI(customer);
                        cartUI.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                loadBasketTable();
                                loadProductTable(null);
                                loadCartTable();
                            }
                        });
                    }
                }
            }
        });
    }

    private void loadBasketTable() {
        Object[] columnProduct = {"ID", "Ürün Adı", "Ürün Codu", "Fiyatı", "Stok"};
        ArrayList<Basket> baskets = this.basketController.findALlBaskets();

        DefaultTableModel cleaeModel = (DefaultTableModel) this.tbl_basket.getModel();
        cleaeModel.setRowCount(0);

        this.tmdl_basket.setColumnIdentifiers(columnProduct);
        int totalPrice = 0;
        for (Basket basket : baskets) {
            Object[] tableObject = {
                    basket.getId(),
                    basket.getProduct().getName(),
                    basket.getProduct().getCode(),
                    basket.getProduct().getPrice(),
                    basket.getProduct().getStock()
            };
            totalPrice += basket.getProduct().getPrice();
            this.tmdl_basket.addRow(tableObject);
        }

        this.lbl_basket_price.setText(totalPrice + " TL");
        this.lbl_basket_count.setText(baskets.size() + " Adet");

        this.tbl_basket.setModel(tmdl_basket);
        this.tbl_basket.getTableHeader().setReorderingAllowed(false);
        this.tbl_basket.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_basket.setEnabled(false);
    }

    private void loadProductButtonEvent() {
        btn_product_new.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductUI productUI = new ProductUI(new Product());
                productUI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadProductTable(null);
                    }
                });
            }
        });


        btn_product_filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Product> filteredProducts = productController.filter(
                        fld_f_product_name.getText(),
                        fld_f_product_code.getText(),
                        (Item) cmb_f_stock_status.getSelectedItem());
                loadProductTable(filteredProducts);
            }
        });

        btn_product_reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fld_f_product_name.setText(null);
                fld_f_product_code.setText(null);
                cmb_f_stock_status.setSelectedItem(null);
                loadProductTable(null);
            }
        });
    }

    private void loadProductPopupMenu() {
        this.tbl_product.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_product.rowAtPoint(e.getPoint());
                tbl_product.setRowSelectionInterval(selectedRow, selectedRow);

            }
        });

        this.popup_product.add("Sepete Ekle ").addActionListener(e -> {
            int selectedId = (int) tbl_product.getValueAt(tbl_product.getSelectedRow(),0);
            Product product = this.productController.findById(selectedId);

            if(product.getStock() <= 0){
                Helper.showMessage("Bu ürün stokta bulunmamaktadır.");
            }else{
                Basket basket = new Basket(product.getId());
                boolean result = false;
                if(this.basketController.save(basket)){
                    Helper.showMessage("done");
                    loadBasketTable();
                }else{
                    Helper.showMessage("error");
                }
            }
        });


        this.popup_product.add("Güncelle").addActionListener(e -> {
            int selectId = (int) tbl_product.getValueAt(tbl_product.getSelectedRow(), 0);
            Product editedProduct = productController.findById(selectId);
            ProductUI productUI = new ProductUI(editedProduct);

            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                    loadBasketTable();
                }
            });

        });
        this.popup_product.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectId = (int) tbl_product.getValueAt(tbl_product.getSelectedRow(), 0);
                boolean result = productController.delete(selectId);

                if (result) {
                    Helper.showMessage("done");
                    loadProductTable(null);
                    loadBasketTable();
                } else {
                    Helper.showMessage("error");
                }
            }
        });

        this.tbl_product.setComponentPopupMenu(popup_product);
    }


    private void loadCustomerButtonEvent() {
        this.btn_customer_new.addActionListener(e -> {
            CustomerUI customerUI = new CustomerUI(new Customer());
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                }
            });
        });

        btn_customerfilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Customer> filteredCustomers = customerController.filter(
                        fld_f_customername.getText(),
                        (Customer.TYPE) cmb_customertype.getSelectedItem());

                loadCustomerTable(filteredCustomers);
            }
        });

        btn_customer_reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCustomerTable(null);
                fld_f_customername.setText(null);
                cmb_customertype.setSelectedItem(null);
            }
        });
    }

    private void loadCustomerPopupMenu() {
        this.tbl_customer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_customer.rowAtPoint(e.getPoint());
                tbl_customer.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });

        this.popup_customer.add("Güncelle").addActionListener(e -> {
            int selectId = (int) tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0);
            Customer editedCustomer = customerController.findById(selectId);
            CustomerUI customerUI = new CustomerUI(editedCustomer);
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                }
            });
        });
        this.popup_customer.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectId = (int) tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0);
                boolean result = customerController.delete(selectId);

                if (result) {
                    Helper.showMessage("done");
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                } else {
                    Helper.showMessage("error");
                }
            }

        });

        this.tbl_customer.setComponentPopupMenu(this.popup_customer);
    }

    private void loadProductTable(ArrayList<Product> products) {
        Object[] columnProduct = {"ID", "Ürün Adı", "Ürün Codu", "Fiyatı", "Stok"};

        if (products == null) {
            products = this.productController.findAllProducts();
        }

        DefaultTableModel cleaeModel = (DefaultTableModel) this.tbl_product.getModel();
        cleaeModel.setRowCount(0);

        this.tmdl_product.setColumnIdentifiers(columnProduct);
        for (Product product : products) {
            Object[] tableObject = {
                    product.getId(),
                    product.getName(),
                    product.getCode(),
                    product.getPrice(),
                    product.getStock()
            };
            this.tmdl_product.addRow(tableObject);
        }
        this.tbl_product.setModel(tmdl_product);
        this.tbl_product.getTableHeader().setReorderingAllowed(false);
        this.tbl_product.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_product.setEnabled(false);
    }

    private void loadCustomerTable(ArrayList<Customer> customers) {
        Object[] columnCustomer = {"ID", "Müşter Adı", "Müşteri Tipi", "Telefon", "E-posta", "Adres"};

        if (customers == null) {
            customers = this.customerController.findAll();
        }

        //Tablo Sıfırlama
        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_customer.getModel();
        clearModel.setRowCount(0);

        this.tmdl_customer.setColumnIdentifiers(columnCustomer);
        for (Customer customer : customers) {
            Object[] tableObject = {
                    customer.getId(),
                    customer.getName(),
                    customer.getType(),
                    customer.getPhone(),
                    customer.getMail(),
                    customer.getAddress()
            };
            this.tmdl_customer.addRow(tableObject);
        }

        this.tbl_customer.setModel(tmdl_customer);
        this.tbl_customer.getTableHeader().setReorderingAllowed(false);
        this.tbl_customer.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_customer.setEnabled(false);
    }
}
