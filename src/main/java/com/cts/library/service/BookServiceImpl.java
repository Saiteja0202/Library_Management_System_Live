package com.cts.library.service;

import com.cts.library.authentication.CurrentUser;
import com.cts.library.exception.ResourceNotFoundException;
import com.cts.library.exception.UnauthorizedAccessException;
import com.cts.library.model.Book;
import com.cts.library.model.Role;
import com.cts.library.repository.BookRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepo bookRepo;
    private CurrentUser currentUser;

    public BookServiceImpl(BookRepo bookRepo, CurrentUser currentUser) {
        this.bookRepo = bookRepo;
        this.currentUser = currentUser;
    }

    public String addBook(Book book) {
    	
    	 if(currentUser.getCurrentUser().getRole()!=Role.ADMIN) {
         	
         	throw new UnauthorizedAccessException("User Not Allowed to Add Book");
         }
    	 
        bookRepo.save(book);
        return "Book has been added successfully.";
    }
    @Transactional
    public String deleteBook(Long id) {
        Book exist = getBookById(id);
        
        if(currentUser.getCurrentUser().getRole()!=Role.ADMIN) {
        	
        	throw new UnauthorizedAccessException("User Not Allowed to Delete Book");
        }
        
        bookRepo.delete(exist);
        
        return "Book has been deleted successfully.";
    }
    @Transactional
    public String updateBook(Long id, Book updated) {
        Book exist = getBookById(id);
        
        if(currentUser.getCurrentUser().getRole()!=Role.ADMIN) {
        	
        	throw new UnauthorizedAccessException("User Not Allowed to Update Book");
        }

        exist.setBookName(updated.getBookName());
        exist.setGenre(updated.getGenre());
        exist.setISBN(updated.getISBN());
        exist.setAuthor(updated.getAuthor());
        exist.setAvailableCopies(updated.getAvailableCopies());
        exist.setYearPublished(updated.getYearPublished());

        bookRepo.save(exist);
        return "Book has been updated successfully.";
    }

    public List<Book> getAllBooks() {
    	System.out.println("hek");
        return bookRepo.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " not found"));
    }

    public List<Book> searchByTitle(String title) {
        return bookRepo.findByBookNameContainingIgnoreCase(title);
    }

    public List<Book> searchByGenre(String genre) {
        return bookRepo.findByGenreIgnoreCase(genre);
    }

    public List<Book> searchByAuthor(String author) {
        return bookRepo.findByAuthorContainingIgnoreCase(author);
    }
}
