package com.cts.library.test;

import com.cts.library.authentication.CurrentUser;
import com.cts.library.exception.UnauthorizedAccessException;
import com.cts.library.model.Book;
import com.cts.library.model.Member;
import com.cts.library.model.Role;
import com.cts.library.repository.BookRepo;
import com.cts.library.service.BookServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepo bookRepo;

    @Mock
    private CurrentUser currentUser;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void testAddBook_AsAdmin_ShouldSucceed() {
        Book book = new Book();
        Member adminUser = new Member();
        adminUser.setRole(Role.ADMIN);

        when(currentUser.getCurrentUser()).thenReturn(adminUser);

        String result = bookService.addBook(book);

        verify(bookRepo, times(1)).save(book);
        assertEquals("Book has been added successfully.", result);

        System.out.println("Book added successfully by ADMIN.");
    }

    @Test
    public void testAddBook_AsNonAdmin_ShouldThrowException() {
        Book book = new Book();
        Member normalUser = new Member();
        normalUser.setRole(Role.MEMBER);

        when(currentUser.getCurrentUser()).thenReturn(normalUser);

        assertThrows(UnauthorizedAccessException.class, () -> bookService.addBook(book));
        verify(bookRepo, never()).save(any(Book.class));

        System.out.println("UnauthorizedAccessException thrown as expected.");
    }
}
