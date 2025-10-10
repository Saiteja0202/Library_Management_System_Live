package com.cts.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long bookId;
	@NotBlank(message = "Book Name is required")
	private String bookName;
	@NotBlank(message = "Book Author is required")
	private String author;
	@NotBlank(message = "Book Genre is required")
	private String genre;
	@NotBlank(message = "Book ISBN is required")
	private String ISBN;
	@Min(value = 1000, message = "Invalid Publication year")
	private int yearPublished;
	@Min(value = 0, message = "Available copies cannot be negative")
	private int availableCopies;

	public Book() {
		super();
	}

	public Book(long bookId, @NotBlank(message = "Book Name is required") String bookName,
			@NotBlank(message = "Book Author is required") String author,
			@NotBlank(message = "Book Genre is required") String genre,
			@NotBlank(message = "Book ISBN is required") String iSBN,
			@Min(value = 1000, message = "Invalid Publication year") int yearPublished,
			@Min(value = 0, message = "Available copies cannot be negative") int availableCopies) {
		super();
		this.bookId = bookId;
		this.bookName = bookName;
		this.author = author;
		this.genre = genre;
		ISBN = iSBN;
		this.yearPublished = yearPublished;
		this.availableCopies = availableCopies;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public int getYearPublished() {
		return yearPublished;
	}

	public void setYearPublished(int yearPublished) {
		this.yearPublished = yearPublished;
	}

	public int getAvailableCopies() {
		return availableCopies;
	}

	public void setAvailableCopies(int availableCopies) {
		this.availableCopies = availableCopies;
	}

	public boolean isAvailable() {
		return availableCopies > 0;
	}

}
