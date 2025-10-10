package com.cts.library.controller;

import com.cts.library.model.Email;
import com.cts.library.model.Notification;
import com.cts.library.service.NotificationService;
import com.cts.library.service.NotificationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;



@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    

 
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping("/get-all")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<?> getNotificationsByMemberId(@PathVariable Long memberId) {
        try {
            List<Notification> notifications = notificationService.getNotificationsByMemberId(memberId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching notifications");
        }
    }

  
 


}
