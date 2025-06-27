package com.home.homepage.entity.modal;

import lombok.Data;

@Data
public class ChangeUserPwd {
    private String username;
    private String oldPassword;
    private String newPassword;
}
