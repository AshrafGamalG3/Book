/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library.management.system.strategy;

import javax.swing.JFrame;
import library.management.system.Login;
import library.management.system.auth.UserDAO;

/**
 *
 * @author AG3
 */
public class RegisterStrategy implements UserActionStrategy{

    @Override
    public void execute(String userName, String email, String password, JFrame frame) {
        UserDAO userDAO = new UserDAO();

        if (userDAO.isEmailExist(email)) {
            javax.swing.JOptionPane.showMessageDialog(frame, "Email already exists! Please choose another one.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        } else {
            userDAO.addUserForRegister(userName, email, password);
            javax.swing.JOptionPane.showMessageDialog(frame, "User added successfully! Please Login.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            frame.setVisible(false); // Hide current registration screen
            Login loginFrame = new Login();
            loginFrame.setVisible(true); // Show login screen
        }
    }
    }
    

