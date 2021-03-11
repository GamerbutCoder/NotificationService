package com.quinbook.notifications.entity;

import lombok.Data;

@Data
public class FriendRequestAcceptanceNotification {
    private String eventType;
    private Friend acceptedBy;
}
