package com.quinbook.notifications.controller;

import com.quinbook.notifications.dto.*;
import com.quinbook.notifications.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class NotificationController {

    @Autowired
    FriendRequestService friendRequestService;


    @PostMapping("/friendRequest")
    public ResponseEntity<FriendRequestResponseDTO> notifyFriendRequest(@RequestBody FriendRequestDTO requestDTO){
        return friendRequestService.notifyUser(requestDTO);
    }

    @PostMapping("/latestNotifications")
    public ResponseEntity<LatestNotificationResponseDTO> fetchLatestHistory(@RequestBody NotificationHistoryRequestDTO requestDTO){
        return friendRequestService.fetchLatestNotifications(requestDTO) ;
    }

    @PostMapping("/notificationHistory")
    public ResponseEntity<NotificationHistoryResponseDTO> fetchHistory(@RequestBody NotificationHistoryRequestDTO requestDTO){
        return friendRequestService.fetchNotificationHistory(requestDTO);
    }

    @PostMapping("/friendRequestAccepted")
    public void frndReqAcc(@RequestBody FriendRequestAcceptanceDTO request){
        friendRequestService.friendRequestAccepted(request);
    }
}
