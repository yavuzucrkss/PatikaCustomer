package core;

import javax.swing.*;

public class Helper {
    public static void setTheme(){
        for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            if(info.getName().equals("Nimbus")){
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static boolean isFieldEmpty(JTextField field){
        return field.getText().trim().isEmpty();
    }

    public static boolean isFieldListEmpty(JTextField[] fields){
        for(JTextField field : fields){
            if(isFieldEmpty(field)){
                return true;
            }
        }
        return false;
    }

    public static boolean isValidEmail(String mail){
        if(mail.trim().isEmpty()) return false;
        if(!mail.contains("@")) return false;

        String[] parts = mail.split("@");
        if(parts.length != 2) return false;

        if(parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) return false;

        if(!parts[1].contains(".")) return false;

        return true;
    }

    public static boolean isNumber(String str){
        if(str == null){
            return false;
        }
        try {
            Integer.parseInt(str);
        }catch (NumberFormatException e){
            return false;
        }

        return true;
    }
    public static void optionPaneDialogTR(){
        UIManager.put("OptionPane.okButtonText", "Tamam");
        UIManager.put("OptionPane.yesButtonText","Evet");
        UIManager.put("OptionPane.noButtonText","Hayır");
    }

    public static void showMessage(String message){
        String msg;
        String title;

        switch (message){
            case "fill":
                msg = "Lütfen tüm alanları doldurunuz";
                title = "Hata";
                break;
            case "done":
                msg = "İşlem Başarılı !";
                title = "Sonuç";
                break;
            case "error":
                msg = "Bir hata oluştu";
                title = "Hata!";
                break;
            default:
                msg = message;
                title = "Mesaj";
                break;
        }
        optionPaneDialogTR();
        JOptionPane.showMessageDialog(null, msg,title,JOptionPane.INFORMATION_MESSAGE);
    }



    public static boolean confirm(String str){
        String msg;
        optionPaneDialogTR();

        switch (str){
            case "sure":
                msg = "Bu işlemi gerçekleştirmek istediğinize emin misiniz ?";
                break;
            default:
                msg = str;
                break;
        }

        return JOptionPane.showConfirmDialog(null,msg,"Emin misin ?",JOptionPane.YES_NO_OPTION) == 0;
    }
}
