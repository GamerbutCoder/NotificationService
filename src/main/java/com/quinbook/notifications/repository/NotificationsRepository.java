package com.quinbook.notifications.repository;

import com.quinbook.notifications.entity.Notifications;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationsRepository extends MongoRepository<Notifications,String> {
}
