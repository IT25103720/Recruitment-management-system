package com.lankajobshub.repository;

import com.lankajobshub.model.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    List<NotificationTemplate> findAllByOrderByUpdatedAtDescCreatedAtDesc();
}
