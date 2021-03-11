package com.quinbook.notifications.service;

import com.quinbook.notifications.dto.*;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

public interface FriendRequestService {
    ResponseEntity<FriendRequestResponseDTO> notifyUser(FriendRequestDTO requestDTO);
    ResponseEntity<LatestNotificationResponseDTO> fetchLatestNotifications(NotificationHistoryRequestDTO requestDTO);
    ResponseEntity<NotificationHistoryResponseDTO> fetchNotificationHistory(NotificationHistoryRequestDTO requestDTO);

}
