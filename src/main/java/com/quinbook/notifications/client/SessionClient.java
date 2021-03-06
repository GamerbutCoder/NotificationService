package com.quinbook.notifications.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//TODO: config feign

@FeignClient(name = "session-client",url = "http://10.177.68.27:8090")
public interface SessionClient {
    @RequestMapping(method = RequestMethod.POST,path = "/fetchUserName")
    String getUserName(@RequestParam("sessionId") String sessionId);
}
