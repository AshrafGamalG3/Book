/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library.management.system.strategy;

import javax.swing.JFrame;
import library.management.system.AdmainPage;
import library.management.system.UserPage;
import library.management.system.auth.UserDAO;

/**
 *
 * @author AG3
 */
public class LoginStrategy implements UserActionStrategy{

public void execute(String userName, String email, String password, javax.swing.JFrame frame) {
        UserDAO userDAO = new UserDAO();

        boolean isAuthenticated = userDAO.isCorrectPassword(email, password);

        if (isAuthenticated) {
            if(email.equals("admain@gmail.com") && password.equals("123456")){
                  AdmainPage dashboardFrame = new AdmainPage();
            dashboardFrame.setVisible(true);
            frame.setVisible(false); // Hide login window
            }
            else{
                 UserPage dashboardFrame = new UserPage();
            dashboardFrame.setVisible(true);
            frame.setVisible(false); // Hide login window
            }
            javax.swing.JOptionPane.showMessageDialog(frame, "Login successful!");
            // Redirect to the dashboard or main application screen
            // For example:
          
        } else {
            javax.swing.JOptionPane.showMessageDialog(frame, "Invalid email or password.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
