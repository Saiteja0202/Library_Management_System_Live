package com.cts.library.controller;

import com.cts.library.exception.UnauthorizedAccessException;
import com.cts.library.model.BorrowingTransaction;
import com.cts.library.model.LoginDetails;
import com.cts.library.model.Member;
import com.cts.library.service.BorrowingTransactionServiceImpl;
import com.cts.library.service.MemberService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Validated
public class MemberController {

    private final MemberService memberService;
	private final BorrowingTransactionServiceImpl borrowingTransactionServiceImpl;

    public MemberController(MemberService memberService,BorrowingTransactionServiceImpl borrowingTransactionServiceImpl) {
        this.memberService = memberService;
        this.borrowingTransactionServiceImpl = borrowingTransactionServiceImpl;
    }

    @PostMapping("/admin/admin-register")
    public ResponseEntity<String> createAdmin(@RequestBody Member admin) {
        return ResponseEntity.ok(memberService.createAdmin(admin));
    }
    @GetMapping("/admin/allmembers")
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/admin/get-member/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }
    
    @PostMapping("/login")
    public ResponseEntity<HashMap<String, String>> loginMember(@RequestBody LoginDetails loginDetails) {

    	HashMap<String, String> response = memberService.loginMember(loginDetails);
        return ResponseEntity.ok(response); 
    }



    @PostMapping("/member/register")
    public ResponseEntity<String> registerMember(@RequestBody Member member) {
    	System.out.println("yeah");
        return ResponseEntity.ok(memberService.registerMember(member));
    }

    @GetMapping("/member/{id}/profile")
    public ResponseEntity<Member> getMemberProfile(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        memberService.updateMembershipStatus(member);
        return ResponseEntity.ok(member);
    }

    @PutMapping("/member/{id}/update")
    public ResponseEntity<String> updateMember(@PathVariable Long id,@RequestBody Member member) {
    	 String result = memberService.updateMember(id, member);
    	 return ResponseEntity
    		        .ok()
    		        .header("Content-Type", "application/json")
    		        .body("\"" + result + "\"");
    }
    
    @PutMapping("/member/{id}/update-password")
    public ResponseEntity<Map<String, String>> updateMemberPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwordMap) {

        String currentPassword = passwordMap.get("currentPassword");
        String newPassword = passwordMap.get("newPassword");

        // Call the service method and handle logic as needed
        memberService.updatePassword(id, currentPassword, newPassword);

        return ResponseEntity.ok(Collections.singletonMap("message", "Password updated successfully"));
    }




    @DeleteMapping("/member/{id}/delete")

    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.deleteMemberById(id));
    }

    @PutMapping("/member/{id}/activate")
    public ResponseEntity<String> activateMembership(@PathVariable Long id,
                                                     @RequestParam int months) {
        return ResponseEntity.ok(memberService.activateMembership(id, months));
    }
    
    @GetMapping("/member/transactions/{id}")
    public ResponseEntity<List<BorrowingTransaction>> getMemberTransactions(@PathVariable Long id) {
        List<BorrowingTransaction> transactions = borrowingTransactionServiceImpl.getTransactions(id);
        return ResponseEntity.ok(transactions);
    }


}
