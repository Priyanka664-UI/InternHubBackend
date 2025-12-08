package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Notification;
import SmartInternshipApp.InternHubBackend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping("/{studentId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long studentId) {
        return ResponseEntity.ok(notificationService.getNotifications(studentId));
    }
    
    @GetMapping("/{studentId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long studentId) {
        return ResponseEntity.ok(notificationService.getUnreadCount(studentId));
    }
    
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Marked as read");
    }
    
    @PutMapping("/{studentId}/read-all")
    public ResponseEntity<String> markAllAsRead(@PathVariable Long studentId) {
        notificationService.markAllAsRead(studentId);
        return ResponseEntity.ok("All notifications marked as read");
    }
    
    @PostMapping
    public ResponseEntity<String> createNotification(@RequestParam Long studentId, @RequestParam String title, @RequestParam String message, @RequestParam String type) {
        notificationService.createNotification(studentId, title, message, type);
        return ResponseEntity.ok("Notification created");
    }
}