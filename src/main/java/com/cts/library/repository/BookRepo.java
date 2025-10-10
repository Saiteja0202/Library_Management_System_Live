package com.cts.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.library.model.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, Long>{
	
	List<Book> findByBookNameContainingIgnoreCase(String title);
	List<Book> findByGenreIgnoreCase(String Genre);
	List<Book> findByAuthorContainingIgnoreCase(String author);
	
}
