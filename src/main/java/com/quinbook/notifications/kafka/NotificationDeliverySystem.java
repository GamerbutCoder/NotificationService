package com.quinbook.notifications.kafka;

import com.quinbook.notifications.constants.StatusMessages;
import com.quinbook.notifications.entity.Friend;
import com.quinbook.notifications.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationDeliverySystem {

    @Autowired
    private FriendRequestService friendRequestService;

    @KafkaListener(topics = "javatopic")
    public void listenMessages(Message<String> message){
        String key = message.getHeaders().get("kafka_receivedMessageKey").toString();
        String[] keys = key.split("--");
        if(StatusMessages.NEW_POST.equals(keys[0])){
            String obj = message.getPayload();
            System.out.println(obj);
            //post object based use object mapper to get get post object and create notification
        }
    }

}
