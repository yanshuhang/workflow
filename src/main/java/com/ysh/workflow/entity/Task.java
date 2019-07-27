package com.ysh.workflow.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {
    private Integer id;
    private Integer boardId;
    private Integer type;
    private Integer color;
    private float order;
    private String title;
    private String description;
    private LocalDateTime createTime;
    private User creator;
    private LocalDateTime lastOperateTime;
}

