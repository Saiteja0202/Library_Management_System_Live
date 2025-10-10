package com.cts.library.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cts.library.model.MemberToken;
import com.cts.library.repository.MemberTokenRepo;

import jakarta.transaction.Transactional;

@Component
public class Scheduler {
		
	private final MemberTokenRepo memberTokenRepo;
	 
    public Scheduler(MemberTokenRepo memberTokenRepo) {
        this.memberTokenRepo = memberTokenRepo;
    }
 
    @Scheduled(fixedRate = 60 * 60 * 1000*24)
    @Transactional
    public void clearExpiredTokens() {
        LocalDateTime expiry = LocalDateTime.now().minusMinutes(60);
        if (expiry!= null) {
 
            List<MemberToken> expiredUsers = memberTokenRepo.findExpiredTokens(expiry);
 
	        for (MemberToken member : expiredUsers) {
	        	memberTokenRepo.deleteById(member.getTokenId());
	        }
        }
     }
}
