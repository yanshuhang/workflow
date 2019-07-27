package com.ysh.workflow.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ysh.workflow.entity.Board;
import com.ysh.workflow.entity.User;

import java.util.List;

public interface BoardService {
    List<User> getUsers(int boardId);

    Board boardInfo(int boardId);

    User addUser(int boardId, String username);

    Board addBoard(Board board);

}
