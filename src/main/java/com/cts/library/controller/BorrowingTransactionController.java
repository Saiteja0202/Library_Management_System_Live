package com.cts.library.controller;

import com.cts.library.model.BorrowingTransaction;
import com.cts.library.model.Book;
import com.cts.library.service.BorrowingTransactionService;
import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/books")
public class BorrowingTransactionController {

    private final BorrowingTransactionService transactionService;

    public BorrowingTransactionController(BorrowingTransactionService transactionService) {
        this.transactionService = transactionService;
    }


	@PostMapping("/borrow/{memberId}/{bookId}")
	public ResponseEntity<String> borrowBook(@PathVariable Long memberId, @PathVariable Long bookId) {
	    String result = transactionService.borrowBook(bookId, memberId); 
	    return ResponseEntity.ok(result); 
	    
	}

    @PostMapping("/return/{memberId}/{bookId}")
    public String returnBook(@PathVariable Long bookId, @PathVariable Long memberId) {
        return transactionService.returnBook(bookId, memberId);
    }

    @GetMapping("/borrowing-transactions")
    public ResponseEntity<List<BorrowingTransaction>> getAllTransactions() {
        List<BorrowingTransaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/get-borrowed-books/{memberId}")
    public List<Book> getAllBorrowedBooks(@PathVariable Long memberId){
        return transactionService.getAllBorrowedBooks(memberId);
    }
}
