package com.ysh.workflow.service;

import com.ysh.workflow.dao.BoardDao;
import com.ysh.workflow.dao.UserDao;
import com.ysh.workflow.entity.Board;
import com.ysh.workflow.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {
    private final BoardDao boardDao;
    private final UserDao userDao;

    public BoardServiceImpl(BoardDao boardDao, UserDao userDao) {
        this.boardDao = boardDao;
        this.userDao = userDao;
    }

    @Override
    public List<User> getUsers(int boardId) {
        return boardDao.getUsers(boardId);
    }

    @Override
    public Board boardInfo(int boardId) {
        return boardDao.selectBoardById(boardId);
    }

    @Override
    public User addUser(int boardId, String username) {
        User user = userDao.selectUserByName(username);
        if (user != null) {
            boardDao.addUser(boardId, user.getId());
        }
        return user;
    }

    @Override
    public Board addBoard(Board board) {
        boardDao.createBoard(board);
        boardDao.addUser(board.getId(), board.getCreator().getId());
        return board;
    }
}
