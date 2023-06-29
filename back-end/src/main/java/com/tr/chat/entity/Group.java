package com.tr.chat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@TableName("contact_group")
public class Group implements Serializable {
    Integer id;
    String name;
    Character nickIndex;
    String avatar;
    Integer number;
    @TableField(exist = false)
    List<GroupUser> userList;


    public List<GroupUser> getUserList() {
        return userList;
    }

    public void setUserList(List<GroupUser> userList) {
        this.userList = userList;
    }

    public Character getNickIndex() {
        return nickIndex;
    }

    public void setNickIndex(Character nickIndex) {
        this.nickIndex = nickIndex;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickIndex=" + nickIndex +
                ", avatar='" + avatar + '\'' +
                ", number=" + number +
                ", userList=" + userList +
                '}';
    }
}
