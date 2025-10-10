package com.cts.library.repository;

import com.cts.library.model.BorrowingTransaction;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.library.model.Book;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingTransactionRepo extends JpaRepository<BorrowingTransaction, Long> {
    List<BorrowingTransaction> findByMember_MemberId(Long memberId);
    void deleteByMember_MemberId(Long memberId);
    boolean existsByBook_BookIdAndStatus(Long bookId, BorrowingTransaction.Status status);
    
    @Query("SELECT bt FROM BorrowingTransaction bt")
    List<BorrowingTransaction> findAllWithMemberAndBook();
    
    
    @Transactional
    @Query("SELECT bt FROM BorrowingTransaction bt WHERE bt.book.bookId = :bookId AND bt.member.memberId = :memberId")
    BorrowingTransaction findByBookIdAndMemberId(@Param("bookId") Long bookId, @Param("memberId") Long memberId);

    boolean existsByMember_MemberId(Long memberId);



    @Query("SELECT b FROM BorrowingTransaction b WHERE b.transactionId = :transactionId")
    BorrowingTransaction findByTransactionId(@Param("transactionId") Long transactionId);

    @Query("SELECT bt.book FROM BorrowingTransaction bt WHERE bt.member.memberId = :memberId AND bt.status = 'BORROWED'")
    List<Book> findBooksByBorrowedTransactions(Long memberId);

}

