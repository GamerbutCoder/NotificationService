package com.quinbook.notifications.entity;

import lombok.Data;

import java.util.List;

@Data
public class Events<T> {
    private String eventType;
    private List<T> notifications;
}
