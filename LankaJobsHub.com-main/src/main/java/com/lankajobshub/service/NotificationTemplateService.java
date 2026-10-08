package com.lankajobshub.service;

import com.lankajobshub.model.NotificationTemplate;
import com.lankajobshub.model.User;
import com.lankajobshub.repository.NotificationTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationTemplateService {

    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    public List<NotificationTemplate> findAll() {
        return notificationTemplateRepository.findAllByOrderByUpdatedAtDescCreatedAtDesc();
    }

    public NotificationTemplate findById(Long id) {
        return notificationTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification template not found with id: " + id));
    }

    public NotificationTemplate save(NotificationTemplate template, User updatedBy) {
        validate(template);
        if (template.getId() != null) {
            NotificationTemplate existing = findById(template.getId());
            existing.setName(clean(template.getName()));
            existing.setTitle(clean(template.getTitle()));
            existing.setMessage(clean(template.getMessage()));
            existing.setType(template.getType());
            existing.setStatus(template.getStatus());
            existing.setScheduledAt(template.getScheduledAt());
            existing.setUpdatedBy(updatedBy);
            existing.setUpdatedAt(LocalDateTime.now());
            return notificationTemplateRepository.save(existing);
        }
        template.setName(clean(template.getName()));
        template.setTitle(clean(template.getTitle()));
        template.setMessage(clean(template.getMessage()));
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());
        template.setUpdatedBy(updatedBy);
        return notificationTemplateRepository.save(template);
    }

    public void delete(Long id) {
        notificationTemplateRepository.delete(findById(id));
    }

    private void validate(NotificationTemplate template) {
        if (isBlank(template.getName())) {
            throw new RuntimeException("Template name is required");
        }
        if (isBlank(template.getTitle())) {
            throw new RuntimeException("Template title is required");
        }
        if (isBlank(template.getMessage())) {
            throw new RuntimeException("Template message is required");
        }
        if (template.getType() == null) {
            throw new RuntimeException("Template type is required");
        }
        if (template.getStatus() == null) {
            throw new RuntimeException("Template status is required");
        }
        if (template.getStatus() == NotificationTemplate.TemplateStatus.SCHEDULED && template.getScheduledAt() == null) {
            throw new RuntimeException("Scheduled templates require a scheduled date and time");
        }
        if (template.getScheduledAt() != null && template.getScheduledAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Scheduled date and time cannot be in the past");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String clean(String value) {
        return value == null ? null : value.trim();
    }
}
