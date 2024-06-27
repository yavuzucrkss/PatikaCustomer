package view;

import business.CustomerController;
import core.Helper;
import entity.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerUI extends JFrame{
    private JPanel container;
    private JTextField fld_customer_name;
    private JLabel lbl_customer_name;
    private JLabel lbl_customer_type;
    private JComboBox<Customer.TYPE> cmb_customer_type;
    private JTextField fld_customer_phone;
    private JLabel lbl_customer_mail;
    private JTextField fld_customer_mail;
    private JLabel lbl_customer_address;
    private JTextArea tarea_customer_address;
    private JButton btn_customer_Save;
    private JLabel lbl_title;
    private Customer customer;
    private CustomerController customerController;

    public CustomerUI(Customer customer){
        this.customer = customer;
        this.customerController = new CustomerController();

        this.add(container);
        this.setTitle("Müşteri Ekle/Düzenle");
        this.setSize(300,500);
        this.setVisible(true);
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height) / 2;
        this.setLocation(x, y);

        this.cmb_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));

        if(this.customer.getId() == 0){
            this.lbl_title.setText("Müşteri Ekle");
        }else{
            this.lbl_title.setText("Müşteri Düzenle");
            this.fld_customer_name.setText(this.customer.getName());
            this.fld_customer_mail.setText(this.customer.getMail());
            this.fld_customer_phone.setText(this.customer.getPhone());
            this.tarea_customer_address.setText(this.customer.getAddress());
            this.cmb_customer_type.getModel().setSelectedItem(this.customer.getType());
        }
        btn_customer_Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField[] checkList = {fld_customer_name,fld_customer_phone};
                if(Helper.isFieldListEmpty(checkList)){
                    Helper.showMessage("fill");
                }else if(!Helper.isFieldEmpty(fld_customer_mail) && !Helper.isValidEmail(fld_customer_mail.getText())){
                    Helper.showMessage("Lütfen geçerli bir mail adresi giriniz.");
                }
                else{
                    boolean result = false;
                    customer.setName(fld_customer_name.getText());
                    customer.setPhone(fld_customer_phone.getText());
                    customer.setMail(fld_customer_mail.getText());
                    customer.setAddress(tarea_customer_address.getText());
                    customer.setType((Customer.TYPE) cmb_customer_type.getSelectedItem());

                    if(customer.getId() == 0){
                        result = customerController.save(customer);
                    }else{
                        result = customerController.update(customer);
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
