package com.ysh.workflow.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Board {
    private Integer id;
    private String name;
    private User creator;
    private LocalDateTime createTime;
    private List<Task> taskList;
    private List<User> userList;
}
