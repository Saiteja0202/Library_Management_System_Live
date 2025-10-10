

package com.cts.library.exception;

public class BorrowingTransactionNotFoundException extends RuntimeException {
    public BorrowingTransactionNotFoundException(String message) {
        super(message);
    }
}
