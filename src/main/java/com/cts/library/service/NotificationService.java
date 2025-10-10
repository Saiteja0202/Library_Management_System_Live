package com.cts.library.service;

import com.cts.library.model.Notification;
import java.util.List;

public interface NotificationService {
    
    public List<Notification> getAllNotifications();
    List<Notification> getNotificationsByMemberId(Long memberId);
    public void generateDueAndOverdueNotifications(); 
}