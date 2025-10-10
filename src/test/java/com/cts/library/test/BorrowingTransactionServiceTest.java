package com.cts.library.test;

import com.cts.library.authentication.CurrentUser;
import com.cts.library.exception.BookNotFoundException;
import com.cts.library.model.Book;
import com.cts.library.model.BorrowingTransaction;
import com.cts.library.model.Member;
import com.cts.library.repository.BookRepo;
import com.cts.library.repository.BorrowingTransactionRepo;
import com.cts.library.repository.MemberRepo;
import com.cts.library.service.BorrowingTransactionServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BorrowingTransactionServiceTest {

    @Mock
    private BorrowingTransactionRepo transactionRepo;

    @Mock
    private BookRepo bookRepo;

    @Mock
    private MemberRepo memberRepo;

    @Mock
    private CurrentUser currentUser;

    @InjectMocks
    private BorrowingTransactionServiceImpl service;

    @Test
    void testBorrowBook_ValidBook_ShouldSucceed() {
        Long bookId = 1L;
        Long memberId = 1L;

        Book book = new Book();
        book.setBookId(bookId);
        book.setAvailableCopies(1);

        Member member = new Member();
        member.setMemberId(memberId);
        member.setBorrowingLimit(2);

        when(bookRepo.findById(bookId)).thenReturn(Optional.of(book));
        when(memberRepo.findById(memberId)).thenReturn(Optional.of(member));
        when(currentUser.getCurrentUser()).thenReturn(member);

        String result = service.borrowBook(bookId, memberId);

        assertEquals("Book borrow pending for approval", result);
        assertEquals(1, book.getAvailableCopies());
        assertEquals(2, member.getBorrowingLimit());
        verify(transactionRepo, times(1)).save(any(BorrowingTransaction.class));
        System.out.println("Book borrow pending for approval.");
    }

    @Test
    void testBorrowBook_BookNotFound_ShouldThrowException() {
        Long bookId = 1L;
        Long memberId = 10L;

        Member member = new Member();
        member.setMemberId(memberId);
        member.setBorrowingLimit(2);

        when(bookRepo.findById(bookId)).thenReturn(Optional.empty());
        when(currentUser.getCurrentUser()).thenReturn(member);

        assertThrows(BookNotFoundException.class, () -> service.borrowBook(bookId, memberId));
        System.out.println("Exception thrown for missing book.");
    }
}
