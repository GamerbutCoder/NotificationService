package com.quinbook.notifications.dto;

import com.quinbook.notifications.entity.Friend;
import lombok.Data;

@Data
public class FriendRequestDTO {
    private String sessionId;
    private String toWhom;
    private Friend selfDetails;
}
