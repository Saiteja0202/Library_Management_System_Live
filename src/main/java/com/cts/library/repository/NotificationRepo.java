package com.cts.library.repository;

import com.cts.library.model.Notification;
import com.cts.library.model.Member;
import com.cts.library.model.Book;
import com.cts.library.model.Fine;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    List<Notification> findByDateSentAfter(Date date);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO notifications (overdue_days, member_id, book_id, message, date_sent)
        SELECT
            0 AS overdue_days,
            bt.member_id,
            bt.book_id,
            'Reminder: "' || b.book_name || '" is due in ' ||
            (bt.return_date - CURRENT_DATE) || ' day(s). Please return on time.' AS message,
            CURRENT_DATE
        FROM borrowing_transaction bt
        JOIN book b ON bt.book_id = b.book_id
        WHERE bt.status IN ('BORROWED', 'RETURN_PENDING', 'RETURN_REJECTED')
          AND (bt.return_date - CURRENT_DATE) <= 3
          AND NOT EXISTS (
              SELECT 1 FROM notifications n
              WHERE n.book_id = bt.book_id
                AND n.member_id = bt.member_id
                AND (bt.return_date - n.date_sent) <= 3
          )
        """, nativeQuery = true)
    int insertDueReminders();

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE notifications n
        SET message = 'Reminder: "' || b.book_name || '" is due in ' ||
                     (bt.return_date - CURRENT_DATE) || ' day(s). Please return on time.',
            date_sent = CURRENT_DATE
        FROM borrowing_transaction bt
        JOIN book b ON bt.book_id = b.book_id
        WHERE n.book_id = bt.book_id
          AND n.member_id = bt.member_id
          AND bt.status IN ('BORROWED', 'RETURN_PENDING', 'RETURN_REJECTED')
          AND (bt.return_date - CURRENT_DATE) BETWEEN 0 AND 3
          AND n.fine_id IS NULL
        """, nativeQuery = true)
    int updateDueReminderMessages();

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE notifications n
        SET message = 'Urgent Reminder: "' || b.book_name || '" is due today. Please return it before the library closes!',
            date_sent = CURRENT_DATE
        FROM borrowing_transaction bt
        JOIN book b ON bt.book_id = b.book_id
        WHERE n.book_id = bt.book_id
          AND n.member_id = bt.member_id
          AND bt.status IN ('BORROWED', 'RETURN_PENDING', 'RETURN_REJECTED')
          AND bt.return_date = CURRENT_DATE
          AND n.fine_id IS NULL
        """, nativeQuery = true)
    int updateReminderToDueTodayMessage();

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE notifications n
        SET overdue_days = (CURRENT_DATE - bt.return_date),
            message = 'Overdue: "' || b.book_name || '" is ' ||
                      (CURRENT_DATE - bt.return_date) || ' day(s) overdue. Fine ₹' || f.amount || '.',
            fine_id = f.fine_id,
            date_sent = CURRENT_DATE
        FROM borrowing_transaction bt
        JOIN fine f ON f.transaction_id = bt.transaction_id
        JOIN book b ON bt.book_id = b.book_id
        WHERE n.book_id = bt.book_id
          AND n.member_id = bt.member_id
          AND bt.status IN ('BORROWED', 'RETURN_PENDING', 'RETURN_REJECTED')
          AND bt.return_date < CURRENT_DATE
          AND LOWER(f.fine_status) = 'pending'
        """, nativeQuery = true)
    int upgradeReminderToOverdue();

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE notifications n
        SET overdue_days = 0,
            message = 'Fine Paid & Book Returned: "' || b.book_name || '" has been returned successfully. ' ||
                      'Your fine has been cleared. Thank you, ' || m.name || ', for staying accountable!',
            date_sent = CURRENT_DATE
        FROM borrowing_transaction bt
        JOIN book b ON bt.book_id = b.book_id
        JOIN member m ON bt.member_id = m.member_id
        WHERE n.book_id = bt.book_id
          AND n.member_id = bt.member_id
          AND bt.status = 'RETURNED'
        """, nativeQuery = true)
    int upgradeOverdueToReturned();

    void deleteByMember_MemberId(long memberId);

    @Query("SELECT n FROM Notification n WHERE n.member.id = :memberId")
    List<Notification> findByMemberId(@Param("memberId") Long memberId);
}
