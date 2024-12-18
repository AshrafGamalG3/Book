/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library.management.system.factory;

import java.util.List;
import library.management.system.command.BookDAO;
import library.management.system.command.BorrowerBook;

/**
 *
 * @author AG3
 */
public class AllLoanedBooks implements BookDisplay {
    @Override
    public List<BorrowerBook> getBooks() {
        return BookDAO.getAllLoanedBooks(); 
    }
}