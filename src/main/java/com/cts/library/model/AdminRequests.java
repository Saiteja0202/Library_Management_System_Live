package com.cts.library.model;

import com.cts.library.model.BorrowingTransaction.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable  // Marks this entity as read-only
@Table(name = "admin_requests_view")
public class AdminRequests {

    @Id
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "book_name")
    private String bookName;

    @Enumerated(EnumType.STRING)
    private BorrowingTransaction.Status status;
    
   
	

    // Constructor
    public AdminRequests() {
    }

    public AdminRequests(Long requestId, Long memberId, String memberName, Long bookId, String bookName, Status status) {
        this.requestId = requestId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.bookId = bookId;
        this.bookName = bookName;
        this.status = status;
    }

    // Getters only (no setters for read-only safety)
    public Long getRequestId() {
        return requestId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public BorrowingTransaction.Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Admin_Requests [requestId=" + requestId + ", memberId=" + memberId + ", memberName=" + memberName
                + ", bookId=" + bookId + ", bookName=" + bookName + ", status=" + status + "]";
    }
}
