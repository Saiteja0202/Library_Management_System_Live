package com.cts.library.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cts.library.exception.FineAlreadyPaidException;
import com.cts.library.exception.FineNotFoundException;
import com.cts.library.model.BorrowingTransaction;
import com.cts.library.model.Fine;
import com.cts.library.repository.BorrowingTransactionRepo;
import com.cts.library.repository.FineRepo;

@Service
public class FineServiceImpl implements FineService {

    private final FineRepo fineRepo;
    private final BorrowingTransactionRepo borrowingTransactionRepo;

    public FineServiceImpl(FineRepo fineRepo, BorrowingTransactionRepo borrowingTransactionRepo) {
        this.fineRepo = fineRepo;
        this.borrowingTransactionRepo = borrowingTransactionRepo;
    }

    @Override
    public List<Fine> getFinesByMemberId(Long memberId) {
        return fineRepo.findByMember_MemberId(memberId);
    }

    
    

   
    @Scheduled(cron = "00 30 11 * * ?") 
    public void processDailyFines() {
        fineRepo.insertPendingFinesForOverdueTransactions();
        fineRepo.updatePendingFineAmountsDaily();
    }
	
	@Override
	public void payFine(Long fineId) {
	    Fine fine = fineRepo.findById(fineId)
	            .orElseThrow(() -> new FineNotFoundException("Fine not found with ID: " + fineId));

	    if ("PAID".equalsIgnoreCase(fine.getFineStatus())) {
	        throw new FineAlreadyPaidException("Fine is already paid.");
	    }
	    
	    fine.setFineStatus("PAID");
	    fineRepo.save(fine);

	    BorrowingTransaction transaction = fine.getTransaction();
	    transaction.setStatus(BorrowingTransaction.Status.RETURNED);
	    transaction.setReturnDate(LocalDate.now());
	    borrowingTransactionRepo.save(transaction);
	}
	
	@Override
	public List<Fine> getAllFines() {
	    return fineRepo.findAllFines();
	}

}
