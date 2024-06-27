package business;

import dao.UserDao;
import entity.User;

import java.sql.SQLException;

public class UserController {
    private final UserDao userDao = new UserDao();


    public User findByLogin(String mail, String password){
        return this.userDao.findByLogin(mail,password);
    }

}
