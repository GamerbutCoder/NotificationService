package com.quinbook.notifications.service.impl;

import com.mongodb.client.MongoClients;
import com.quinbook.notifications.client.SessionClient;
import com.quinbook.notifications.constants.ConstantStrings;
import com.quinbook.notifications.dto.*;
import com.quinbook.notifications.entity.Friend;
import com.quinbook.notifications.entity.FriendRequestAcceptanceNotification;
import com.quinbook.notifications.entity.FriendRequestNotification;
import com.quinbook.notifications.entity.Notifications;
import com.quinbook.notifications.repository.NotificationsRepository;
import com.quinbook.notifications.service.FriendRequestService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestServiceIMPL implements FriendRequestService {

    static MongoOperations mongoOperations = new MongoTemplate(MongoClients.create(),"quinbook");

    @Autowired
    private SessionClient sessionClient;

    @Autowired
    private NotificationsRepository notificationsRepository;

    //                String toUserName = sessionClient.getUserName(requestDTO.getSessionId());

    @Override
    public ResponseEntity<FriendRequestResponseDTO> notifyUser(FriendRequestDTO requestDTO) {

        FriendRequestResponseDTO response = new FriendRequestResponseDTO();

        if(requestDTO.getSelfDetails() == null || requestDTO.getSessionId() == null  || requestDTO.getSessionId().length()==0){
            response.setMessage("FAILED");
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        else{
            String userName = requestDTO.getSelfDetails().getUserName(); //if name is not avail then use sessionclient to fetch
            Notifications notification;
            Optional<Notifications> optional = notificationsRepository.findById(requestDTO.getToWhom());
            FriendRequestNotification friendRequestNotification = new FriendRequestNotification();
            Friend friend = new Friend();
            BeanUtils.copyProperties(requestDTO.getSelfDetails(),friend);
            friendRequestNotification.setStatus(ConstantStrings.NOT_YET_INTERACTED);
            friendRequestNotification.setEventType(ConstantStrings.FRIEND_REQUEST);
            friendRequestNotification.setFrom(userName);
            friendRequestNotification.setUserBaseProfile(friend);
            if(optional.isPresent()){
                Query query = new Query();
                Criteria criteria = Criteria.where("userName").is(requestDTO.getToWhom());
                query.addCriteria(criteria);
                Update update = new Update().push("latestNotification",friendRequestNotification);
                mongoOperations.updateFirst(query,update,Notifications.class);
                update = new Update().push("notificationHistory",friendRequestNotification);
                mongoOperations.updateFirst(query,update,Notifications.class);

            }
            else{
                notification = new Notifications();
                notification.setUserName(requestDTO.getToWhom());
                List l = new ArrayList();
                l.add(friendRequestNotification);
                notification.setLatestNotification(l);
                l = new ArrayList();
                notification.setNotificationHistory(l);
                notificationsRepository.save(notification);
            }

            response.setMessage("SUCCESS");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<LatestNotificationResponseDTO> fetchLatestNotifications(NotificationHistoryRequestDTO requestDTO) {
        ResponseEntity<LatestNotificationResponseDTO> response;
        LatestNotificationResponseDTO latestNotificationResponseDTO = new LatestNotificationResponseDTO();
        try{
             String userName = sessionClient.getUserName(requestDTO.getSessionId());
            //String userName = "abcd";
            Optional<Notifications> optional = notificationsRepository.findById(userName);
            if(optional.isPresent()){
                Notifications notifications = optional.get();
                List notif = new ArrayList();
                for(Object o:notifications.getLatestNotification()){
                    notif.add(o);
                }
                latestNotificationResponseDTO.setLatestNotifications(notif);
                Query query = new Query();
                Criteria criteria = Criteria.where("userName").is(userName);
                query.addCriteria(criteria);
                Update update = new Update().set("latestNotification",new ArrayList());

                mongoOperations.updateFirst(query,update,Notifications.class);
                response = new ResponseEntity<>(latestNotificationResponseDTO,HttpStatus.OK);
                return response;
            }
            else{
                latestNotificationResponseDTO.setLatestNotifications(new ArrayList());
                response = new ResponseEntity<>(latestNotificationResponseDTO, HttpStatus.OK);
                return response;
            }
        }
        catch (Exception e){
            response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return response;
        }

    }

    @Override
    public ResponseEntity<NotificationHistoryResponseDTO> fetchNotificationHistory(NotificationHistoryRequestDTO requestDTO) {
        ResponseEntity<NotificationHistoryResponseDTO> response;
        NotificationHistoryResponseDTO notificationHistoryResponseDTO = new NotificationHistoryResponseDTO();
        try{
            String userName = sessionClient.getUserName(requestDTO.getSessionId());
           // String userName = "abcd";
            Optional<Notifications> optional = notificationsRepository.findById(userName);
            if(optional.isPresent()){
                Notifications notifications = optional.get();
                List notif = new ArrayList();
                for(Object o:notifications.getNotificationHistory()){
                    notif.add(o);
                }
                notificationHistoryResponseDTO.setNotificationHistory(notif);
                response = new ResponseEntity<>(notificationHistoryResponseDTO,HttpStatus.OK);
                return response;
            }
            else{
                response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                return response;
            }
        }
        catch (Exception e){
            response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return response;
        }
    }

    @Override
    public void friendRequestAccepted(FriendRequestAcceptanceDTO request) {
        Notifications notifications = new Notifications();
        if(request.getAcceptedBy() == null || request.getEventType() == null || request.getEventType().length()==0){
            return;
        }

        else{
            String userName = request.getWhose();
            try{
                Optional<Notifications> optional = notificationsRepository.findById(userName);
                FriendRequestAcceptanceNotification obj = new FriendRequestAcceptanceNotification();
                obj.setEventType(ConstantStrings.FRIEND_REQUEST_ACCEPT);
                obj.setAcceptedBy(request.getAcceptedBy());
                if(optional.isPresent()){
                    Query query = new Query();
                    Criteria criteria = Criteria.where("userName").is(userName);
                    query.addCriteria(criteria);
                    Update update = new Update().push("latestNotification",obj);
                    mongoOperations.updateFirst(query,update,Notifications.class);
                    update = new Update().push("notificationHistory",obj);
                    mongoOperations.updateFirst(query,update,Notifications.class);

                }
                else{
                    notifications.setUserName(userName);
                    List l = new ArrayList();
                    l.add(obj);
                    notifications.setNotificationHistory(l);
                    l = new ArrayList();
                    l.add(obj);
                    notifications.setLatestNotification(l);
                    notificationsRepository.save(notifications);
                }

            }
            catch (Exception e){

            }
        }
    }
}
