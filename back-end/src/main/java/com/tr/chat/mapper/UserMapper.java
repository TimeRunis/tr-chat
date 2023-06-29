package com.tr.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tr.chat.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select id,nick,name,avatar from contact_user where id=#{id}")
    User getSimUserInfo(String id);
}
