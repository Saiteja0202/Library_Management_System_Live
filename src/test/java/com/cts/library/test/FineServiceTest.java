package com.cts.library.test;

import com.cts.library.exception.FineNotFoundException;
import com.cts.library.exception.FineAlreadyPaidException;
import com.cts.library.model.Fine;
import com.cts.library.repository.FineRepo;
import com.cts.library.repository.BorrowingTransactionRepo;
import com.cts.library.service.FineServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FineServiceTest {

    @Mock
    private FineRepo fineRepo;

    @Mock
    private BorrowingTransactionRepo transactionRepo;

    @InjectMocks
    private FineServiceImpl fineService;

    private Fine getFineById(Long id) {
        return fineRepo.findById(id)
                .orElseThrow(() -> new FineNotFoundException("Fine with ID " + id + " not found."));
    }

    @Test
    void testGetFineById_Exists() {
        Fine fine = new Fine();
        fine.setFineId(1L);

        when(fineRepo.findById(1L)).thenReturn(Optional.of(fine));

        Fine result = getFineById(1L);
        assertEquals(1L, result.getFineId());
        System.out.println("Fine retrieved successfully.");
    }

    @Test
    void testGetFineById_NotFound() {
        when(fineRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(FineNotFoundException.class, () -> getFineById(2L));
        System.out.println("Exception thrown as expected.");
    }

    @Test
    void testPayFine_AlreadyPaid() {
        Fine fine = new Fine();
        fine.setFineId(1L);
        fine.setFineStatus("PAID");

        when(fineRepo.findById(1L)).thenReturn(Optional.of(fine));

        assertThrows(FineAlreadyPaidException.class, () -> fineService.payFine(1L));
        System.out.println("Exception thrown for already paid fine.");
    }
}
