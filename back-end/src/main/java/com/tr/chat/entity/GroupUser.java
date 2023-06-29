package com.tr.chat.entity;

import com.baomidou.mybatisplus.annotation.TableField;

public class GroupUser {
    @TableField(exist = false)
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
