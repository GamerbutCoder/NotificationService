package com.quinbook.notifications.dto;

import com.quinbook.notifications.entity.Friend;
import lombok.Data;

@Data
public class FriendRequestAcceptanceDTO {
    private String eventType;
    private Friend acceptedBy;
    private String whose;
}
