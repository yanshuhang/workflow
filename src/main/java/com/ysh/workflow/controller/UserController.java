package com.ysh.workflow.controller;

import com.ysh.workflow.entity.Board;
import com.ysh.workflow.entity.User;
import com.ysh.workflow.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("register")
    public String register(@Valid @RequestBody User user) {
        return service.register(user);
    }

    @PostMapping("login")
    public String login(@RequestBody User user) {
        return service.login(user);
    }

    @PostMapping("autoLogin")
    public boolean autoLogin(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return service.autoLogin(token);
    }

    @GetMapping("{userId}/boards")
    public List<Board> getBoards(@PathVariable int userId) {
        return service.getBoards(userId);
    }

    @PostMapping("/inviteUser")
    public void invite(@RequestBody Map<String, User> userMap) {
        User inviter = userMap.get("inviter");
        User invitee = userMap.get("invitee");
        service.inviteUser(inviter, invitee);
    }

    @PostMapping("/acceptInvite")
    public Board acceptInvite(@RequestBody Map<String, User> userMap) {
        User inviter = userMap.get("inviter");
        User invitee = userMap.get("invitee");
        return service.acceptInvite(inviter, inviter.getBoardList().get(0), invitee);
    }

    @PostMapping("/rejectInvite")
    public void rejectInvite(@RequestBody Map<String, User> userMap) {
        User inviter = userMap.get("inviter");
        User invitee = userMap.get("invitee");
        service.rejectInvite(inviter, invitee);
    }
}
