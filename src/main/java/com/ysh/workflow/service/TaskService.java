package com.ysh.workflow.service;

import com.ysh.workflow.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> getTasks(int boardId);

    void updateOrder(Task task, int userId);

    void modifyTask(Task task, int userId);

    void updateType(Task task, int userId);

    void deteleTask(int id);

    Task addTask(Task task);
}
