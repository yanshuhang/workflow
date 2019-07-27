package com.ysh.workflow.dao;

import com.ysh.workflow.entity.Board;
import com.ysh.workflow.entity.Task;
import com.ysh.workflow.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardDao {
    void createBoard(Board board);

    void rename(Board board);

    Board selectBoardById(int id);

    void addUser(@Param("boardId") int boardId, @Param("userId") int userId);

    List<User> getUsers(int boardId);
}
