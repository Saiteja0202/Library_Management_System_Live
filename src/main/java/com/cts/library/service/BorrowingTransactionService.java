package com.cts.library.service;

import java.util.List;

import com.cts.library.model.Book;
import com.cts.library.model.BorrowingTransaction;

public interface BorrowingTransactionService {
    String borrowBook(Long bookId, Long memberId);
    String returnBook(Long bookId, Long memberId);
    public List<BorrowingTransaction> getTransactions(Long memberId);
    public List<BorrowingTransaction> getAllTransactions();
    public List<Book> getAllBorrowedBooks(Long memberId);

}