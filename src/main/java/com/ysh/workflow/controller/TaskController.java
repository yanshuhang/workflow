package com.ysh.workflow.controller;

import com.ysh.workflow.entity.Task;
import com.ysh.workflow.service.TaskService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("{userId}/updateOrder")
    public void updateOrder(@RequestBody Task task, @PathVariable("userId") int userId) {
        taskService.updateOrder(task, userId);
    }

    @PostMapping("{userId}/updateType")
    public void updateType(@RequestBody Task task, @PathVariable("userId") int userId) {
        taskService.updateType(task, userId);
    }

    @DeleteMapping("delete/{taskId}")
    public void deleteTask(@PathVariable int taskId) {
        taskService.deteleTask(taskId);
    }

    @PostMapping("{userId}/modifyTask")
    public void modifyTask(@RequestBody Task task, @PathVariable("userId") int userId) {
        taskService.modifyTask(task, userId);
    }

    @PostMapping("addTask")
    public Task addTask(@RequestBody Task task) {
        return taskService.addTask(task);
    }
}
