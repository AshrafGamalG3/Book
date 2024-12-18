/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library.management.system.command;

/**
 *
 * @author AG3
 */
public class BorrowerBook {
    private int id;
    private String author;
    private String borrowerEmail;
    private int bookId;
    private String bookName;

    // Constructor
    public BorrowerBook(int id, String author, String borrowerEmail, int bookId, String bookName) {
        this.id = id;
        this.author = author;
        this.borrowerEmail = borrowerEmail;
        this.bookId = bookId;
        this.bookName = bookName;
    }

    // Overloaded Constructor (for use when ID is auto-generated)
    public BorrowerBook(String author, String borrowerEmail, int bookId, String bookName) {
        this.author = author;
        this.borrowerEmail = borrowerEmail;
        this.bookId = bookId;
        this.bookName = bookName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBorrowerName() {
        return author;
    }

    public void setBorrowerName(String borrowerName) {
        this.author = borrowerName;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    public void setBorrowerEmail(String borrowerEmail) {
        this.borrowerEmail = borrowerEmail;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

 
}
