package com.ysh.workflow.service;

import com.ysh.workflow.entity.Board;
import com.ysh.workflow.entity.User;

import java.util.List;

public interface UserService {
    String register(User user);

    String login(User user);

    boolean autoLogin(String token);

    List<Board> getBoards(int userId);

    void inviteUser(User inviter, User invitee);

    Board acceptInvite(User inviter, Board board, User invitee);

    void rejectInvite(User inviter, User invitee);
}
