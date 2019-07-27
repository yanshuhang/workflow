package com.ysh.workflow.dao;

import com.ysh.workflow.entity.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskDao {
    void addTask(Task task);

    void updateTask(Task task);

    void updateOrder(Task task);

    void updateType(Task task);

    void deleteTask(int id);

    List<Task> getTasks(int boardId);
}
