/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library.management.system.factory;

/**
 *
 * @author AG3
 */
public class BookDisplayFactory {
    public static BookDisplay getBookDisplay() {
        return new AllLoanedBooks();
    }
}
