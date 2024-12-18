package library.management.system.command;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import library.management.system.singleton.DatabaseConnection;

public class BookDAO {

    // Using try-with-resources to handle connections, statements, and result sets
    private static Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    // Add a new book
    public static void addBook(Book book) {
        String query = "INSERT INTO books (book_name, author, copies_count) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, book.getBookName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getCopiesCount());
            preparedStatement.executeUpdate();
            System.out.println("Book added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a book by name
   public static void deleteBookById(int bookId) {
    String query = "DELETE FROM books WHERE book_id = ?";
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        // تعيين الـ ID للـ PreparedStatement
        preparedStatement.setInt(1, bookId);
        // تنفيذ الاستعلام
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Book with ID " + bookId + " deleted successfully.");
        } else {
            System.out.println("No book found with ID " + bookId);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // Retrieve all books
    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String bookName = resultSet.getString("book_name");
                String author = resultSet.getString("author");
                int copiesCount = resultSet.getInt("copies_count");
                books.add(new Book(id, bookName, author, copiesCount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    
    public void updateBookCopies(int bookId) {
     // Update query with correct column name
     String query = "UPDATE Books SET copies_count = copies_count - 1 WHERE book_id = ?";
     try (Connection conn = getConnection();
          PreparedStatement stmt = conn.prepareStatement(query)) {
         stmt.setInt(1, bookId);  // Set the book's ID as parameter
         stmt.executeUpdate();  // Execute the update
     } catch (SQLException e) {
         e.printStackTrace();  // Print the exception stack trace for debugging
     }
 }

     
     public void addLoanRecord(int bookId, String bookName, String borrowerName, String borrowerEmail) {
    String query = "INSERT INTO LoanedBooks (book_id, book_name, borrower_name, borrower_email) VALUES (?, ?, ?, ?)";
    
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, bookId);
        stmt.setString(2, bookName);
        stmt.setString(3, borrowerName);
        stmt.setString(4, borrowerEmail);

        stmt.executeUpdate();
        System.out.println("Loan record added successfully.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
     
        public static List<BorrowerBook> getAllLoanedBooks() {
        List<BorrowerBook > books = new ArrayList<>();
        String query = "SELECT * FROM LoanedBooks";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
               int id = resultSet.getInt("id");
            String borrowerName = resultSet.getString("borrower_name");
            String borrowerEmail = resultSet.getString("borrower_email");
            int bookId = resultSet.getInt("book_id");
            String bookName = resultSet.getString("book_name");
          System.out.println("ID: " + id + ", Borrower Name: " + borrowerName + ", Borrower Email: " + borrowerEmail +
                       ", Book ID: " + bookId + ", Book Name: " + bookName);
            
                books.add(new BorrowerBook(id,borrowerName, borrowerEmail, bookId, bookName));
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    


    
}
