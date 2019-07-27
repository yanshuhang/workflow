package com.ysh.workflow.controller;

import com.ysh.workflow.entity.Board;
import com.ysh.workflow.entity.Task;
import com.ysh.workflow.entity.User;
import com.ysh.workflow.service.BoardService;
import com.ysh.workflow.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("board")
public class BoardController {
    private final BoardService boardService;
    private final TaskService taskService;

    public BoardController(BoardService boardService, TaskService taskService) {
        this.boardService = boardService;
        this.taskService = taskService;
    }

    @GetMapping("{boardId}/getUsers")
    public List<User> getUsers(@PathVariable int boardId) {
        return boardService.getUsers(boardId);
    }

    @GetMapping("{boardId}/getTasks")
    public List<Task> getTasks(@PathVariable int boardId) {
        return taskService.getTasks(boardId);
    }

    @GetMapping("{boardId}/boardInfo")
    public Board boardInfo(@PathVariable int boardId) {
        return boardService.boardInfo(boardId);
    }

    @PostMapping("{boardId}/addUser")
    public User addUser(@PathVariable int boardId, @RequestBody User user) {
        @NotBlank(message = "用户名不能为空") String name = user.getName();
        return boardService.addUser(boardId, name);
    }

    @PostMapping("addBoard")
    public Board addBoard(@RequestBody Board board) {
        return boardService.addBoard(board);
    }
}
