package com.quinbook.notifications.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@Document(collection = "notifications")
public class Notifications {
    @MongoId
    private String userName;
    private List latestNotification;
    private List notificationHistory;
//    private List<T> newFriendRequestNotification;
//    private List<T> newPostNotification;
//    private List<FriendRequestNotification> friendRequestNotificationsHistory;
//    private List<LatestPostNotification> latestPostNotificationsHistory;
}
