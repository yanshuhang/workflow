package com.ysh.workflow.dao;

import com.ysh.workflow.entity.Board;
import com.ysh.workflow.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    void addUser(User user);

    User selectUserById(int id);

    User selectUserByName(String name);

    List<Board> getBoards(int userId);

    void addBoard(@Param("user") User user, @Param("board") Board board);
}
