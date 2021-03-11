package com.quinbook.notifications.entity;

import lombok.Data;


@Data
public class FriendRequestNotification {
    private String from;
    private String status;
    private Friend userBaseProfile;
    private String eventType;
}
