package com.cts.library.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.library.authentication.CurrentUser;
import com.cts.library.exception.ResourceNotFoundException;
import com.cts.library.exception.UnauthorizedAccessException;
import com.cts.library.model.LoginDetails;
import com.cts.library.model.Member;
import com.cts.library.model.MemberToken;
import com.cts.library.model.MembershipStatus;
import com.cts.library.model.Role;
import com.cts.library.repository.BorrowingTransactionRepo;
import com.cts.library.repository.FineRepo;
import com.cts.library.repository.MemberRepo;
import com.cts.library.repository.MemberTokenRepo;
import com.cts.library.repository.NotificationRepo;

import jakarta.transaction.Transactional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepo memberRepo;
    private final BorrowingTransactionRepo transactionRepo;
    private final FineRepo fineRepo;
    private final NotificationRepo notificationRepo;
    private final MemberTokenRepo memberTokenRepo;
    private final CurrentUser currentUser;

    public MemberServiceImpl(MemberRepo memberRepo,
    		BorrowingTransactionRepo transactionRepo, 
    		FineRepo fineRepo,
    		NotificationRepo notificationRepo,
    		MemberTokenRepo memberTokenRepo,
    		CurrentUser currentUser) {
        this.memberRepo = memberRepo;
        this.memberTokenRepo = memberTokenRepo;
        this.currentUser = currentUser;
        this.transactionRepo = transactionRepo;
        this.fineRepo = fineRepo;
        this .notificationRepo = notificationRepo;
    }


    public String registerMember(Member member) {
        if (memberRepo.existsByUsername(member.getUsername())) {
            throw new RuntimeException("Username already taken.");
        }

        member.setRole(Role.MEMBER);
        member.setPassword(hashPassword(member.getPassword()));
        member.setMembershipStatus(MembershipStatus.BASIC);
        memberRepo.save(member);
        return "Member registered successfully.";
    }

    @Transactional
    public String createAdmin(Member newAdmin) {
    	if (memberRepo.existsByUsername(newAdmin.getUsername())) {  
            throw new RuntimeException("Username already taken.");
        }
        newAdmin.setRole(Role.ADMIN);
        newAdmin.setPassword(hashPassword(newAdmin.getPassword()));
        
        memberRepo.save(newAdmin);
      
        return "Admin created successfully.";
        
    }

     @Transactional
    public String updateMember(Long id, Member updated) {
    	 
    	 if (currentUser.getCurrentUser().getMemberId() != id) {
    		 throw new UnauthorizedAccessException("You are not allowed to update other's profile");
    	 }
    	 
        Member existing = getMemberById(id);

       
        
        if(currentUser.getCurrentUser() == null) {
        	throw new UnauthorizedAccessException("Please Login");
        }

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        memberRepo.save(existing);

        return "Member profile updated.";
    }
     @Transactional
     public String updatePassword(Long id, String currentPassword, String newPassword) {
         Member existing = getMemberById(id);
         

         if (currentUser.getCurrentUser() == null) {
             throw new UnauthorizedAccessException("Please login.");
         }

        
         
         if (!verifyPassword(currentPassword, existing.getPassword())) {
             throw new IllegalArgumentException("Current password is incorrect.");
         }
         System.out.println(existing.getMemberId());
         existing.setPassword(hashPassword(newPassword));
         memberRepo.save(existing);

         return "Password updated successfully";
     }

    
    private boolean verifyPassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword); 
    }

    
    @Transactional
    public String UpdateRole(Long id, Long adminId) {
        Member member = getMemberById(id);
        if(currentUser.getCurrentUser() == null) {
        	throw new UnauthorizedAccessException("Please Login");
        }
        if (currentUser.getCurrentUser().getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("Only admins can promote members.");
        }

        member.setRole(Role.ADMIN);
        memberRepo.save(member);

        return "Congrats, you have been promoted to ADMIN.";
    }

    @Transactional
    public String deleteMemberById(Long id) {
        Member member = getMemberById(id);

        if (currentUser.getCurrentUser() == null) {
            throw new UnauthorizedAccessException("Please Login");
        }

      

        String responseMessage="";
        boolean hasTransactions = transactionRepo.existsByMember_MemberId(id);
        if (hasTransactions) {
        	responseMessage="Cannot delete account: You have existing borrowing transactions.";
        }
        else {
        	
        	memberTokenRepo.deleteByMember_MemberId(id);
            notificationRepo.deleteByMember_MemberId(id);
            fineRepo.deleteByMember_MemberId(id);
            memberRepo.delete(member);
            responseMessage="Member deleted successfully.";
        }
        return responseMessage;
    }


     
    public List<Member> getAllMembers() {
    	if(currentUser.getCurrentUser() == null) {
        	throw new UnauthorizedAccessException("Please Login");
        }
    	 if (currentUser.getCurrentUser().getRole() != Role.ADMIN) {
             throw new UnauthorizedAccessException("You are not allowed to view other's details");
         }
        return memberRepo.findAll();
    }

     
    public Member getMemberById(Long id) {
    	if(currentUser.getCurrentUser() == null) {
        	throw new UnauthorizedAccessException("Please Login");
        }
    	 if (currentUser.getCurrentUser().getRole() != Role.ADMIN && currentUser.getCurrentUser().getMemberId() != id) {
             throw new UnauthorizedAccessException("You are not allowed to view other person profile");
         }
    	
        return memberRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Member with ID " + id + " not found."));
    }

     
    public String activateMembership(Long id, int months) {
        Member member = getMemberById(id);
        if(currentUser.getCurrentUser() == null) {
        	throw new UnauthorizedAccessException("Please Login");
        }
        member.setMembershipStatus(MembershipStatus.PRIME);

        LocalDate newExpiry = (member.getMembershipExpiryDate() == null)
                ? LocalDate.now().plusMonths(months)
                : member.getMembershipExpiryDate().plusMonths(months);

        member.setMembershipExpiryDate(newExpiry);
        member.setBorrowingLimit(member.getBorrowingLimit()+10);
        memberRepo.save(member);
        return "Membership activated to Prime until " + newExpiry + ".";
    }

    
    public void updateMembershipStatus(Member member) {
    	if(currentUser.getCurrentUser() == null) {
        	throw new UnauthorizedAccessException("Please Login");
        }
        if (member.getMembershipExpiryDate() != null &&
            LocalDate.now().isAfter(member.getMembershipExpiryDate())) {

            member.setMembershipStatus(MembershipStatus.EXPIRED);
            memberRepo.save(member);
        }
    }


    public HashMap<String, String> loginMember(LoginDetails loginDetails) {
    	
        Member member = memberRepo.findByUsername(loginDetails.getUserName());
        
        if (member == null) {
        	
            throw new UnauthorizedAccessException("Member not found.");
        }
        

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        if (!encoder.matches(loginDetails.getUserPassword(), member.getPassword())) {
        	
            throw new UnauthorizedAccessException("Invalid credentials23.");
        }

        Optional<MemberToken> existingToken = memberTokenRepo.findByMember(member);
        HashMap<String, String> response = new HashMap<>();
        response.put("memberId", String.valueOf(member.getMemberId()));
        response.put("role", member.getRole().name());
        
        if (existingToken.isPresent()) {
        	response.put("token", existingToken.get().getMemberToken());
            return response;
        }

        MemberToken memberToken = new MemberToken();
        String randomToken=UUID.randomUUID().toString();
        memberToken.setMemberToken(randomToken);
        memberToken.setGeneratedAt(LocalDateTime.now());
        memberToken.setMember(member);
        memberTokenRepo.save(memberToken);

        response.put("token", randomToken);
       
        return response;
    }


    private String hashPassword(String plainText) {
        return new BCryptPasswordEncoder().encode(plainText);
    }
    
    @Scheduled(cron = "0 0 0 * * *") 
    public void checkMembershipExpiry() {
        List<Member> allMembers = memberRepo.findAll();
        LocalDate today = LocalDate.now();

        for (Member member : allMembers) {
            if (member.getMembershipStatus() == MembershipStatus.PRIME &&
                member.getMembershipExpiryDate() != null &&
                today.isAfter(member.getMembershipExpiryDate())) {

                member.setMembershipStatus(MembershipStatus.BASIC);
                member.setMembershipExpiryDate(null);
                member.setBorrowingLimit(member.getBorrowingLimit() - 10); 
                memberRepo.save(member);

                System.out.println("Membership expired for member ID: " + member.getMemberId());
            }
        }
    }
}
