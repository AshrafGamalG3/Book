/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library.management.system.auth;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import library.management.system.singleton.DatabaseConnection;

public class UserDAO {
    
    private Connection connection;
    
    public UserDAO() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // دالة للتحقق من وجود البريد الإلكتروني في قاعدة البيانات
    public boolean isEmailExist(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // دالة لإضافة المستخدم
    public  void addUserForRegister(String userName, String email, String pass) {
        String query = "INSERT INTO user (username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userName);
            statement.setString(2, email);
            statement.setString(3, pass);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
       public boolean isCorrectPassword(String email, String password) {
        String query = "SELECT password FROM user WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
               
                String storedPassword = resultSet.getString("password");

                
                if (storedPassword.equals(password)) {
                    return true; // Passwords match
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Passwords do not match or user does not exist
    }
    
    
}