package com.ysh.workflow.entity;

import lombok.Data;

import java.util.HashMap;

@Data
public class WebSocketMessage {
    private int type;
    private HashMap<String, Object> map;
}
