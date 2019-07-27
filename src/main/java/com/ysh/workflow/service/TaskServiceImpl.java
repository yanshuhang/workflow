package com.ysh.workflow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysh.workflow.component.WebSocketSessionManager;
import com.ysh.workflow.dao.TaskDao;
import com.ysh.workflow.entity.ResponseData;
import com.ysh.workflow.entity.Task;
import com.ysh.workflow.util.WebSocketSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskDao taskDao;
    private final WebSocketSessionManager webSocketSessionManager;
    private final  WebSocketSessionUtil webSocketSessionUtil;
    private final ObjectMapper objectMapper;

    public TaskServiceImpl(TaskDao taskDao, WebSocketSessionManager webSocketSessionManager, WebSocketSessionUtil webSocketSessionUtil, ObjectMapper objectMapper) {
        this.taskDao = taskDao;
        this.webSocketSessionManager = webSocketSessionManager;
        this.webSocketSessionUtil = webSocketSessionUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Task> getTasks(int boardId) {
        return taskDao.getTasks(boardId);
    }

    @Override
    public void updateOrder(Task task, int userId) {
        taskDao.updateOrder(task);
        List<Session> boardSessions = webSocketSessionManager.getBoardSessions(task.getBoardId());
        Session userSession = webSocketSessionManager.getSessionById(userId);
        ResponseData responseData = new ResponseData();
        responseData.setCode(102);
        responseData.setMsg("移动了任务位置");
        HashMap<String, Object> data = new HashMap<>();
        data.put("task", task);
        responseData.setData(data);
        for (Session session : boardSessions) {
            if (session != userSession) {
                webSocketSessionUtil.sendObject(session, responseData, objectMapper);
            }
        }
    }

    @Override
    public void modifyTask(Task task, int userId) {
        taskDao.updateTask(task);
        List<Session> boardSessions = webSocketSessionManager.getBoardSessions(task.getBoardId());
        Session userSession = webSocketSessionManager.getSessionById(userId);
        ResponseData responseData = new ResponseData();
        responseData.setCode(103);
        responseData.setMsg("修改了任务");
        HashMap<String, Object> data = new HashMap<>();
        data.put("task", task);
        responseData.setData(data);
        for (Session session : boardSessions) {
            if (session != userSession) {
                webSocketSessionUtil.sendObject(session, responseData, objectMapper);
            }
        }
    }

    @Override
    public void updateType(Task task, int userId) {
        taskDao.updateType(task);
        List<Session> boardSessions = webSocketSessionManager.getBoardSessions(task.getBoardId());
        Session userSession = webSocketSessionManager.getSessionById(userId);
        ResponseData responseData = new ResponseData();
        responseData.setCode(105);
        responseData.setMsg("移动任务");
        HashMap<String, Object> data = new HashMap<>();
        data.put("task", task);
        responseData.setData(data);
        for (Session session : boardSessions) {
            if (session != userSession) {
                webSocketSessionUtil.sendObject(session, responseData, objectMapper);
            }
        }
    }

    @Override
    public void deteleTask(int id) {
        taskDao.deleteTask(id);
    }

    @Override
    public Task addTask(Task task) {
        taskDao.addTask(task);
        List<Session> boardSessions = webSocketSessionManager.getBoardSessions(task.getBoardId());
        Session userSession = webSocketSessionManager.getSessionById(task.getCreator().getId());
        ResponseData responseData = new ResponseData();
        responseData.setCode(104);
        responseData.setMsg("创建任务");
        HashMap<String, Object> data = new HashMap<>();
        data.put("task", task);
        responseData.setData(data);
        for (Session session : boardSessions) {
            if (session != userSession) {
                webSocketSessionUtil.sendObject(session, responseData, objectMapper);
            }
        }
        return task;
    }
}
