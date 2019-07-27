package com.ysh.workflow.controller;

import com.ysh.workflow.component.WebSocketSessionManager;
import com.ysh.workflow.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@Slf4j
@Component
@ServerEndpoint("/websocket/{userId}/{boardId}")
public class FirstWebSocketServer {
    private int userId;
    private int boardId;
    private static WebSocketSessionManager webSocketSessionManager;
    private static BoardService boardService;

    @Autowired
    public void setBoardService(BoardService boardService) {
        FirstWebSocketServer.boardService = boardService;
    }

    @Autowired
    public void setWebSocketMapManager(WebSocketSessionManager webSocketSessionManager) {
        FirstWebSocketServer.webSocketSessionManager = webSocketSessionManager;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") int userId, @PathParam("boardId") int boardId) {
        this.userId = userId;
        this.boardId = boardId;
        webSocketSessionManager.addSession(userId, boardId ,session);
    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        log.info("session: " + session.getId() + " send msg: " + msg);
    }

    @OnClose
    public void onClose(Session session) {
        webSocketSessionManager.removeSession(userId, boardId, session);
        log.info("用户 {} 用户session {} 断开连接board {} 成功, 用户总数 {}", userId, session, boardId, webSocketSessionManager.sessionAmount());
    }

}
