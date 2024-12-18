/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library.management.system.command;

/**
 *
 * @author AG3
 */
public class AddBookCommand implements Command{

   private Book book;

    public AddBookCommand(Book book) {
        this.book = book;
    }

    @Override
    public void execute() {
        BookDAO.addBook(book);
    }
    
}
