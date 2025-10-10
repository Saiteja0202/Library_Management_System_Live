package com.cts.library.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cts.library.exception.BookNotFoundException;
import com.cts.library.exception.MemberNotFoundException;
import com.cts.library.model.AdminRequests;
import com.cts.library.model.Book;
import com.cts.library.model.BorrowingTransaction;
import com.cts.library.model.Member;
import com.cts.library.repository.AdminRequestsRepo;
import com.cts.library.repository.BookRepo;
import com.cts.library.repository.BorrowingTransactionRepo;
import com.cts.library.repository.MemberRepo;

@Service
public class AdminRequestServiceImpl implements AdminRequestService {

    
    private final BorrowingTransactionRepo borrowingTransactionRepo;

    
    private final AdminRequestsRepo adminRequestsRepo;
    
    
    private final BookRepo bookRepo;
    private final MemberRepo memberRepo;
    
    
   

    public AdminRequestServiceImpl(BorrowingTransactionRepo borrowingTransactionRepo,
			AdminRequestsRepo adminRequestsRepo, BookRepo bookRepo, MemberRepo memberRepo) {
		super();
		this.borrowingTransactionRepo = borrowingTransactionRepo;
		this.adminRequestsRepo = adminRequestsRepo;
		this.bookRepo = bookRepo;
		this.memberRepo = memberRepo;
	}

	@Override
    public List<AdminRequests> getAllRequests() {
        return adminRequestsRepo.findAll();
    }
	
	private String responseMessage;

	
    @Override
    public Map<String, String> acceptRequest(Long transactionId, Long memberId, Long bookId) {
        BorrowingTransaction transaction = borrowingTransactionRepo.findByTransactionId(transactionId);
        Map<String, String> response = new HashMap<>();

        if (transaction == null) {
            response.put("message", "Borrowing transaction not found.");
            return response;
        }
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with ID: " + memberId));
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
   

        switch (transaction.getStatus()) {
            case PENDING:
                if(book.getAvailableCopies() > 0)
                {
                	transaction.setStatus(BorrowingTransaction.Status.BORROWED);
                    borrowingTransactionRepo.save(transaction);
                    book.setAvailableCopies(book.getAvailableCopies() - 1);
                    bookRepo.save(book);
                    member.setBorrowingLimit(member.getBorrowingLimit() - 1);
                    memberRepo.save(member);
                    responseMessage="Request Accepted";
                    response.put("message",responseMessage);
                    return response;
                }
                else {
                	transaction.setStatus(BorrowingTransaction.Status.BORROW_REJECTED);
                	 borrowingTransactionRepo.save(transaction);
                	responseMessage="Sorry, there is no available books.";
                	response.put("message",responseMessage);
                	return response;	
                }
            case RETURN_PENDING:
            	
            		transaction.setReturnDate(LocalDate.now());
                    transaction.setStatus(BorrowingTransaction.Status.RETURNED);
                    borrowingTransactionRepo.save(transaction);
                    book.setAvailableCopies(book.getAvailableCopies() + 1);
                    bookRepo.save(book);
                    member.setBorrowingLimit(member.getBorrowingLimit() + 1);
                    memberRepo.save(member);
                    responseMessage="Request Accepted";
                    response.put("message", responseMessage);
                    return response;
            	
            default:
                response.put("message", "Invalid status for accept action: " + transaction.getStatus());
                return response;
        }
    }

    @Override
    public Map<String, String> rejectRequest(Long transactionId, Long memberId, Long bookId) {
        BorrowingTransaction transaction = borrowingTransactionRepo.findByTransactionId(transactionId);
        
        
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
        Map<String, String> response = new HashMap<>();

        if (transaction == null) {
            response.put("message", "Borrowing transaction not found.");
            return response;
        }

        switch (transaction.getStatus()) {
       
            case PENDING:
            	 if(book.getAvailableCopies() > 0)
             	{
                transaction.setStatus(BorrowingTransaction.Status.BORROW_REJECTED);
                borrowingTransactionRepo.save(transaction);
                response.put("message", "Request Rejected");
                return response;
    	}
    	else {
    		 transaction.setStatus(BorrowingTransaction.Status.BORROW_REJECTED);
    		 borrowingTransactionRepo.save(transaction);
    		 responseMessage="Sorry, there is no available books.";
             response.put("message", responseMessage);
             return response;
    	}
            case RETURN_PENDING:
                transaction.setStatus(BorrowingTransaction.Status.RETURN_REJECTED);
                borrowingTransactionRepo.save(transaction);
                response.put("message", "Request Rejected");
                return response;
            default:
                response.put("message", "Invalid status for reject action: " + transaction.getStatus());
                return response;
        }
    }
}
