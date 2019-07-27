package com.ysh.workflow.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketSessionManager {
    /**
     * 记录所有在线用户的 session
     */
    private ConcurrentHashMap<Integer, Session> allSessions = new ConcurrentHashMap<>();
    /**
     * 根据用户当前boardId 记录用户的 session
     */
    private ConcurrentHashMap<Integer, List<Session>> boardSessions = new ConcurrentHashMap<>();

    /**
     * 添加用户session
     *
     * @param userid  用户id
     * @param boardId 用户当前board id
     * @param session 用户session
     */
    public void addSession(int userid, int boardId, Session session) {
        putToAllSessions(userid, session);
        putToBoardSessions(boardId, session);
    }

    /**
     * 删除用户的session
     *
     * @param userId  用户id
     * @param boardId board id
     * @param session 用户session
     */
    public void removeSession(int userId, int boardId, Session session) {
        allSessions.remove(userId);
        removeSessionFromBoard(boardId, session);
    }

    /**
     * 获得单个用户的 session
     *
     * @param userId 用户id
     * @return 用户session
     */
    public Session getSessionById(int userId) {
        return allSessions.get(userId);
    }

    /**
     * 获得连接该board 的所有用户的 session
     *
     * @param boardId board id
     * @return 连接该board 的所有用户的 session 列表
     */
    public List<Session> getBoardSessions(int boardId) {
        List<Session> sessions = boardSessions.get(boardId);
        if (sessions == null) {
            return Collections.emptyList();
        }
        return sessions;
    }

    /**
     * 当前所有在线用户数量
     *
     * @return 在线用户数量
     */
    public int sessionAmount() {
        return allSessions.size();
    }

    /**
     * 正在使用该board的在线用户数量
     *
     * @param boardId board id
     * @return 正在使用该board的在线用户数量
     */
    public int sessionInBoard(int boardId) {
        return boardSessions.get(boardId).size();
    }

    private void putToAllSessions(int userId, Session session) {
        allSessions.put(userId, session);
        log.info("用户 {} 连接成功，用户总数 {}", userId, sessionAmount());
    }

    private void putToBoardSessions(int boardId, Session session) {
        List<Session> sessions;
        sessions = boardSessions.computeIfAbsent(boardId, k -> new ArrayList<>());
        sessions.add(session);
        log.info("session:: {} 加入board {}, 当前用户数量 {}", session, boardId, sessions.size());
    }

    private void removeSessionFromBoard(int boardId, Session session) {
        List<Session> sessions = getBoardSessions(boardId);
        if (sessions.size() != 0) {
            sessions.remove(session);
            log.info("session:: {} 离开board {}", session, boardId);
            if (sessions.size() == 0) {
                boardSessions.remove(boardId);
                log.info("board {} 没有session连接删除", boardId);
            }
        }
        log.info("session:: {} 离开board {}, 当前用户数量 {}", session, boardId, sessions.size());
    }
}
