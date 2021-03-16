package com.quinbook.notifications.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClients;
import com.quinbook.notifications.constants.ConstantStrings;
import com.quinbook.notifications.entity.LatestPostNotification;
import com.quinbook.notifications.entity.Notifications;
import com.quinbook.notifications.repository.NotificationsRepository;
import com.quinbook.notifications.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class NotificationDeliverySystem {

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private NotificationsRepository notificationsRepository;

    static MongoOperations mongoOperations = new MongoTemplate(MongoClients.create(),"quinbook");

    @KafkaListener(topics = "notifications")
    public void listenMessages(Message<String> message){
        System.out.println(message.getPayload()+" "+message.getHeaders());
        String key = message.getHeaders().get("kafka_receivedMessageKey").toString();
        String[] keys = key.split("--");
        if(ConstantStrings.NEW_POST.equals(keys[0])){
            String obj = message.getPayload();
            //System.out.println(obj);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                LatestPostNotification newPost = objectMapper.readValue(obj, LatestPostNotification.class);
                for(int i=1;i<keys.length;++i){
                    String userName = keys[i];
                    Optional<Notifications> optional = notificationsRepository.findById(userName);
                    if(optional.isPresent()){
                        Query query = new Query();
                        Criteria criteria = Criteria.where("userName").is(userName);
                        query.addCriteria(criteria);
                        Update update = new Update().push("latestNotification",newPost);
                        mongoOperations.updateFirst(query,update, Notifications.class);
                        update = new Update().push("notificationHistory",newPost);
                        mongoOperations.updateFirst(query,update, Notifications.class);
                    }
                    else{
                        Notifications notifications = new Notifications();
                        notifications.setUserName(userName);
                        List latestNotification = new ArrayList();
                        latestNotification.add(newPost);
                        notifications.setLatestNotification(latestNotification);
                        List notificationHistory = new ArrayList();
                        notificationHistory.add(newPost);
                        notifications.setNotificationHistory(notificationHistory);
                        notificationsRepository.save(notifications);
                    }


                }
            }
            catch (Exception e){

            }

        }
    }

}
