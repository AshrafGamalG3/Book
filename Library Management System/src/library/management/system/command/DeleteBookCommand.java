/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library.management.system.command;

/**
 *
 * @author AG3
 */
public class DeleteBookCommand implements Command{

 private int id;

    public DeleteBookCommand(int id) {
        this.id = id;
    }

    @Override
    public void execute() {
        BookDAO.deleteBookById(id);
    }
    
}
