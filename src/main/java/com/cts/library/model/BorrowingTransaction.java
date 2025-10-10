package com.cts.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class BorrowingTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;

	@ManyToOne
	@JoinColumn(name = "bookId", nullable = false)
	private Book book;

	@ManyToOne
	@JoinColumn(name = "memberId", nullable = false)
	@JsonBackReference
	private Member member;

	private LocalDate borrowDate;
	private LocalDate returnDate;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Status status;

	public enum Status {
		BORROW_REJECTED,PENDING,BORROWED,RETURN_PENDING, RETURNED,RETURN_REJECTED
	}
	

	// Constructors
	public BorrowingTransaction() {
	}

	public BorrowingTransaction(Book book, Member member, LocalDate borrowDate, LocalDate returnDate, Status status) {
		this.book = book;
		this.member = member;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
		this.status = status;
	}

	// Getters and Setters\

	
	  @JsonProperty("memberId")
	    public Long getMemberId() {
	        return member != null ? member.getMemberId() : null;
	    }
	  
	   @JsonProperty("memberName")  
	    public String getMemberName() {
	        return member != null ? member.getName() : null;  
	    }

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public LocalDate getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(LocalDate borrowDate) {
		this.borrowDate = borrowDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
