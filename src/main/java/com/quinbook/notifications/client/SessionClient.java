package com.quinbook.notifications.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//TODO: config feign

@FeignClient(name = "session-client",url = "//yet to give")
public interface SessionClient {
    @RequestMapping(method = RequestMethod.POST,path = "//yet to decide")
    public String getUserName(@RequestParam("sessionId") String sessionId);
}
