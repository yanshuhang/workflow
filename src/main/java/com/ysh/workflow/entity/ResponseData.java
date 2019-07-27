package com.ysh.workflow.entity;

import lombok.Data;

import java.util.Map;

@Data
public class ResponseData {
    private int code;
    private String msg;
    private Map<String, Object> data;
}
