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
public class AddLoanRecordState implements OperationState {
    private int bookId;
    private String bookName;
    private String author;
    private String borrowerEmail;

    public AddLoanRecordState(int bookId, String bookName, String author, String borrowerEmail) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.borrowerEmail = borrowerEmail;
    }

    @Override
    public void execute() {
        BookDAO bookDAO = new BookDAO();
        bookDAO.addLoanRecord(bookId, bookName, author, borrowerEmail);
        System.out.println("Loan record added successfully.");
    }
}
