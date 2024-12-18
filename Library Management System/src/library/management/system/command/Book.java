/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library.management.system.command;

/**
 *
 * @author AG3
 */
public class Book {
    private int id;
    private String bookName;
    private String author;
    private int copiesCount;

    public Book(String bookName, String author, int copiesCount) {
        this.bookName = bookName;
        this.author = author;
        this.copiesCount = copiesCount;
    }

    public Book(int id, String bookName, String author, int copiesCount) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.copiesCount = copiesCount;
    }

    public int getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public int getCopiesCount() {
        return copiesCount;
    }
}
