package com.cts.library.service;

import java.util.List;

import com.cts.library.model.Book;

import jakarta.transaction.Transactional;


public interface BookService {
	
	public String addBook(Book book);
	
	@Transactional
	public String deleteBook(Long id);
	
	@Transactional
	public String updateBook(Long id, Book book);
	public Book getBookById(Long id);
	public List<Book> getAllBooks();
	public List<Book> searchByTitle(String title);
	public List<Book> searchByGenre(String genre);
	public List<Book> searchByAuthor(String author);

}
