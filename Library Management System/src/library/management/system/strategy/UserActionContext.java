/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library.management.system.strategy;

/**
 *
 * @author AG3
 */
public class UserActionContext {
     private UserActionStrategy strategy;

    public void setStrategy(UserActionStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeStrategy(String userName, String email, String password, javax.swing.JFrame frame) {
        strategy.execute(userName, email, password, frame);
    }
    
}
