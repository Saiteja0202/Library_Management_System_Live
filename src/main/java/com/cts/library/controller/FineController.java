package com.cts.library.controller;

import com.cts.library.model.Fine;
import com.cts.library.service.FineService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/fines")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @PostMapping("/pay/{fineId}")
    public ResponseEntity<Map<String, String>> payFine(@PathVariable Long fineId) {
        fineService.payFine(fineId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Fine paid successfully.");
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/{memberId}")
    public ResponseEntity<List<Fine>> getFinesByMemberId(@PathVariable Long memberId) {
        List<Fine> fines = fineService.getFinesByMemberId(memberId);
        return ResponseEntity.ok(fines);
    }
    
    
    @GetMapping("/all-fines")
    public ResponseEntity<List<Fine>> getAllFines() {
        List<Fine> fines = fineService.getAllFines();
        return ResponseEntity.ok(fines);
    }


}
