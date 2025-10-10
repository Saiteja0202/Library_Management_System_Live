//package com.cts.library.controller;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.cts.library.model.Admin_Requests;
//import com.cts.library.model.BorrowingTransaction;
//import com.cts.library.repository.AdminRequestRepository;
//import com.cts.library.repository.BorrowingTransactionRepo;
//
//@RestController
//@RequestMapping("/admin")
//public class AdminRequestController {
//
//    @Autowired
//    private BorrowingTransactionRepo borrowingTransactionRepo;
//
//    @Autowired
//    private AdminRequestRepository adminRequestsRepo;
//
//    @GetMapping("/requests")
//    public ResponseEntity<List<Admin_Requests>> getAllTransactions() {
//        List<Admin_Requests> requests = adminRequestsRepo.findAllAdminRequests(); // Query the Admin_Requests view
//        return ResponseEntity.ok(requests);  // Return all requests
//    }
//
//    @PostMapping(value = "/accept/{transactionId}/{memberId}/{bookId}", produces = "application/json")
//    public ResponseEntity<Map<String, String>> acceptRequest(
//        @PathVariable Long transactionId,
//        @PathVariable Long memberId,
//        @PathVariable Long bookId) {
//
//        BorrowingTransaction transaction = borrowingTransactionRepo.findByTransactionId(transactionId);
//        Map<String, String> response = new HashMap<>();
//
//        if (transaction == null) {
//            response.put("message", "Borrowing transaction not found.");
//            return ResponseEntity.status(404).body(response);
//        }
//
//        switch (transaction.getStatus()) {
//            case PENDING:
//                transaction.setStatus(BorrowingTransaction.Status.BORROWED);
//                borrowingTransactionRepo.save(transaction);
//                response.put("message", "Request accepted: status changed from PENDING → BORROWED");
//                return ResponseEntity.ok(response);
//            case RETURN_PENDING:
//                transaction.setStatus(BorrowingTransaction.Status.RETURNED);
//                borrowingTransactionRepo.save(transaction);
//                response.put("message", "Request accepted: status changed from RETURN_PENDING → RETURNED");
//                return ResponseEntity.ok(response);
//            default:
//                response.put("message", "Invalid status for accept action: " + transaction.getStatus());
//                return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @PostMapping(value = "/reject/{transactionId}/{memberId}/{bookId}", produces = "application/json")
//    public ResponseEntity<Map<String, String>> rejectRequest(
//        @PathVariable Long transactionId,
//        @PathVariable Long memberId,
//        @PathVariable Long bookId) {
//
//        BorrowingTransaction transaction = borrowingTransactionRepo.findByTransactionId(transactionId);
//        Map<String, String> response = new HashMap<>();
//
//        if (transaction == null) {
//            response.put("message", "Borrowing transaction not found.");
//            return ResponseEntity.status(404).body(response);
//        }
//
//        switch (transaction.getStatus()) {
//            case PENDING:
//                transaction.setStatus(BorrowingTransaction.Status.BORROW_REJECTED);
//                borrowingTransactionRepo.save(transaction);
//                response.put("message", "Request rejected: status changed from PENDING → BORROW_REJECTED");
//                return ResponseEntity.ok(response);
//            case RETURN_PENDING:
//                transaction.setStatus(BorrowingTransaction.Status.RETURN_REJECTED);
//                borrowingTransactionRepo.save(transaction);
//                response.put("message", "Request rejected: status changed from RETURN_PENDING → RETURN_REJECTED");
//                return ResponseEntity.ok(response);
//            default:
//                response.put("message", "Invalid status for reject action: " + transaction.getStatus());
//                return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//}

package com.cts.library.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.library.model.AdminRequests;
import com.cts.library.repository.AdminRequestsRepo;
import com.cts.library.service.AdminRequestService;

@RestController
@RequestMapping("/admin")
public class AdminRequestController {

	private final AdminRequestsRepo adminRequestsRepo;
	private final AdminRequestService adminRequestService;


	public AdminRequestController(AdminRequestsRepo adminRequestsRepo, AdminRequestService adminRequestService) {
	        this.adminRequestsRepo = adminRequestsRepo;
	        this.adminRequestService = adminRequestService;
	    }

	@GetMapping("/requests")
	public ResponseEntity<List<AdminRequests>> getAllTransactions() {
		List<AdminRequests> requests = adminRequestsRepo.findAll(); // Query the Admin_Requests view
		return ResponseEntity.ok(requests);
	} // Return all requests

	@PostMapping(value = "/accept/{transactionId}/{memberId}/{bookId}", produces = "application/json")
	public ResponseEntity<Map<String, String>> acceptRequest(@PathVariable Long transactionId,
			@PathVariable Long memberId, @PathVariable Long bookId) {

		Map<String, String> response = adminRequestService.acceptRequest(transactionId, memberId, bookId);

		if (response.get("message").contains("not found")) {
			return ResponseEntity.status(404).body(response);
		} else if (response.get("message").startsWith("Invalid")) {
			return ResponseEntity.badRequest().body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/reject/{transactionId}/{memberId}/{bookId}", produces = "application/json")
	public ResponseEntity<Map<String, String>> rejectRequest(@PathVariable Long transactionId,
			@PathVariable Long memberId, @PathVariable Long bookId) {

		Map<String, String> response = adminRequestService.rejectRequest(transactionId, memberId, bookId);

		if (response.get("message").contains("not found")) {
			return ResponseEntity.status(404).body(response);
		} else if (response.get("message").startsWith("Invalid")) {
			return ResponseEntity.badRequest().body(response);
		}

		return ResponseEntity.ok(response);
	}
}
