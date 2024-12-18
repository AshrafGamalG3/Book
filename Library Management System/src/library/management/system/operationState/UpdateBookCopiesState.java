/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library.management.system.operationState;

import library.management.system.command.BookDAO;

/**
 *
 * @author AG3
 */
public class UpdateBookCopiesState implements OperationState {
    private int bookId;

    public UpdateBookCopiesState(int bookId) {
        this.bookId = bookId;
    }

    @Override
    public void execute() {
        BookDAO bookDAO = new BookDAO();
        bookDAO.updateBookCopies(bookId);
        System.out.println("Book copies updated successfully.");
    }
}
