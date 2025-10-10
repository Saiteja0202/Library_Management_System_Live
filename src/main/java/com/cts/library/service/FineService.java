package com.cts.library.service;

import java.util.List;

import com.cts.library.model.Fine;

public interface FineService {

	void processDailyFines();

	void payFine(Long fineId);
	List<Fine> getFinesByMemberId(Long memberId);
	
	List<Fine> getAllFines();


}
