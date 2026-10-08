package com.lankajobshub.service;

import com.lankajobshub.model.Notification;
import com.lankajobshub.model.Notification.NotificationType;
import com.lankajobshub.model.User;
import com.lankajobshub.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    public Notification createNotification(User recipient, String title, String message, NotificationType type) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public List<Notification> findForUser(Long userId) {
        requireUser(userId);
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId);
    }

    public Notification findOwnedById(Long id, Long userId) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        if (!notification.getRecipient().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to access this notification");
        }
        return notification;
    }

    public Notification markRead(Long id, Long userId) {
        Notification notification = findOwnedById(id, userId);
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    public Notification markUnread(Long id, Long userId) {
        Notification notification = findOwnedById(id, userId);
        notification.setRead(false);
        return notificationRepository.save(notification);
    }

    public void delete(Long id, Long userId) {
        Notification notification = findOwnedById(id, userId);
        notificationRepository.delete(notification);
    }

    private User requireUser(Long userId) {
        return userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
}
