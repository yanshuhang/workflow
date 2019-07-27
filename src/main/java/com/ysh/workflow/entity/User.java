package com.ysh.workflow.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class User {
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    private String name;
    @Length(min = 6, max = 20, message = "密码6-20位之间")
    private String password;
    private List<Board> boardList;
}
