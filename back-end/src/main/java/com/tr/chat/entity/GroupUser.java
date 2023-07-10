package com.tr.chat.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

public class GroupUser implements Serializable {
    @TableField(exist = false)
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
