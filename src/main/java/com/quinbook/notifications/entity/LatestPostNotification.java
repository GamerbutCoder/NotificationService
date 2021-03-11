package com.quinbook.notifications.entity;

import lombok.Data;

@Data
public class LatestPostNotification {
    private Friend postedBy;
    private String postId;
}
