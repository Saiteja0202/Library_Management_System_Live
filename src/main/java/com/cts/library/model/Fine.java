package com.cts.library.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @JsonBackReference
    private Member member;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private BorrowingTransaction transaction;

    private double amount;
    private String fineStatus;
    private LocalDate transactionDate;
    
    @Column
    private Long bookId;

    public Long getBookId() {
        return this.transaction != null && this.transaction.getBook() != null
            ? this.transaction.getBook().getBookId()
            : null;
    }
    
    
    @JsonProperty("memberId")
    public Long getMemberId() {
        return member != null ? member.getMemberId() : null;
    }

    

    public void setBookId(Long bookId) {
		this.bookId = bookId;
	}


	public Fine() {
    }

    public Fine(Long fineId, Member member, double amount, String fineStatus, LocalDate transactionDate) {
        this.fineId = fineId;
        this.member = member;
        this.amount = amount;
        this.fineStatus = fineStatus;
        this.transactionDate = transactionDate;
    }

    public Long getFineId() {
        return fineId;
    }

    public void setFineId(Long fineId) {
        this.fineId = fineId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public BorrowingTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(BorrowingTransaction transaction) {
        this.transaction = transaction;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFineStatus() {
        return fineStatus;
    }

    public void setFineStatus(String fineStatus) {
        this.fineStatus = fineStatus;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}
