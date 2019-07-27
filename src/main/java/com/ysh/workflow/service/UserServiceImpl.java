package com.ysh.workflow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysh.workflow.component.WebSocketSessionManager;
import com.ysh.workflow.dao.BoardDao;
import com.ysh.workflow.dao.UserDao;
import com.ysh.workflow.entity.Board;
import com.ysh.workflow.entity.ResponseData;
import com.ysh.workflow.entity.User;
import com.ysh.workflow.util.TokenUtil;
import com.ysh.workflow.util.WebSocketSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final BoardDao boardDao;
    private final WebSocketSessionManager webSocketSessionManager;
    private final WebSocketSessionUtil webSocketSessionUtil;
    private final ObjectMapper objectMapper;

    public UserServiceImpl(UserDao userDao, BoardDao boardDao, WebSocketSessionManager webSocketSessionManager,
                           WebSocketSessionUtil webSocketSessionUtil, ObjectMapper objectMapper) {
        this.userDao = userDao;
        this.boardDao = boardDao;
        this.webSocketSessionManager = webSocketSessionManager;
        this.webSocketSessionUtil = webSocketSessionUtil;
        this.objectMapper = objectMapper;
    }

    /**
     * @param user 用户信息
     * @return 注册成功生成返回token
     */
    @Override
    public String register(User user) {
        if (userDao.selectUserByName(user.getName()) != null) {
            return "该用户名已注册";
        }
        userDao.addUser(user);
        // 注册后自动创建一个board
        Board board = new Board();
        board.setCreator(user);
        board.setName("first board");
        boardDao.createBoard(board);
        boardDao.addUser(board.getId(), user.getId());
        return TokenUtil.createToken(user.getId(), user.getName(), user.getPassword());
    }

    /**
     * @param user 用户信息
     * @return 登录成功生成返回token
     */
    @Override
    public String login(User user) {
        User userWithName = userDao.selectUserByName(user.getName());

        if (userWithName != null) {
            if (user.getPassword().equals(userWithName.getPassword())) {
                return TokenUtil.createToken(userWithName.getId(), userWithName.getName(), userWithName.getPassword());
            }
        }
        return "用户名或密码错误";
    }

    /**
     * 自动登录验证客户端传来的token
     *
     * @param token 客户端token
     * @return true成功
     */
    @Override
    public boolean autoLogin(String token) {
        return TokenUtil.verifyToken(token);
    }

    @Override
    public List<Board> getBoards(int userId) {
        return userDao.getBoards(userId);
    }

    /**
     * 邀请用户加入 inviter 的当前 board
     *
     * @param inviter 邀请者
     * @param invitee 被邀请者
     */
    @Override
    public void inviteUser(User inviter, User invitee) {
        User user = userDao.selectUserByName(invitee.getName());
        ResponseData responseData = new ResponseData();
        Session inviterSession = null;
        Session inviteeSession = null;
        if (user == null) {
            responseData.setCode(100);
            responseData.setMsg("该用户不存在");
            inviterSession = webSocketSessionManager.getSessionById(inviter.getId());
        } else {
            List<User> users = boardDao.getUsers(inviter.getBoardList().get(0).getId());
            boolean alreadyJoin = false;
            for (User user1 : users) {
                if (user.getName().equals(user1.getName())) {
                    responseData.setCode(100);
                    responseData.setMsg("该用户已加入，不能重复邀请");
                    alreadyJoin = true;
                    inviterSession = webSocketSessionManager.getSessionById(inviter.getId());
                    break;
                }
            }
            if (!alreadyJoin) {
                if ((inviteeSession = webSocketSessionManager.getSessionById(user.getId())) == null) {
                    responseData.setCode(100);
                    responseData.setMsg("该用户不在线");
                    inviterSession = webSocketSessionManager.getSessionById(inviter.getId());
                } else {
                    responseData.setCode(400);
                    responseData.setMsg("用户邀请");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("inviter", inviter);
                    responseData.setData(map);
                }
            }
        }
        if (inviteeSession == null) {
            webSocketSessionUtil.sendObject(inviterSession, responseData, objectMapper);
        } else {
            webSocketSessionUtil.sendObject(inviteeSession, responseData, objectMapper);
        }
    }

    @Override
    public Board acceptInvite(User inviter, Board board, User invitee) {
        userDao.addBoard(invitee, board);
        Session inviterSession = webSocketSessionManager.getSessionById(inviter.getId());
        ResponseData responseData = new ResponseData();
        responseData.setCode(101);
        responseData.setMsg(invitee.getName() + "接受了你的邀请");
        webSocketSessionUtil.sendObject(inviterSession, responseData, objectMapper);
        return board;
    }

    @Override
    public void rejectInvite(User inviter, User invitee) {
        Session inviterSession = webSocketSessionManager.getSessionById(inviter.getId());
        ResponseData responseData = new ResponseData();
        responseData.setCode(100);
        responseData.setMsg(invitee.getName() + "拒绝了你的邀请");
        webSocketSessionUtil.sendObject(inviterSession, responseData, objectMapper);
    }
}
