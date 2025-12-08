package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Notification;
import SmartInternshipApp.InternHubBackend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    public List<Notification> getNotifications(Long studentId) {
        return notificationRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
    }
    
    public long getUnreadCount(Long studentId) {
        return notificationRepository.countByStudentIdAndIsReadFalse(studentId);
    }
    
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
    
    public void markAllAsRead(Long studentId) {
        List<Notification> unreadNotifications = notificationRepository.findByStudentIdAndIsReadFalseOrderByCreatedAtDesc(studentId);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }
    
    public void createNotification(Long studentId, String title, String message, String type) {
        Notification notification = new Notification();
        notification.setStudentId(studentId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notificationRepository.save(notification);
    }
}