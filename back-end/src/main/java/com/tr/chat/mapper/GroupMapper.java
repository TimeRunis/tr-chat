package com.tr.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tr.chat.entity.Group;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {
    @Select("select * from contact_group where id=#{groupId}")
    @Results(
            {
                    @Result(column = "id",property = "id"),
                    @Result(column = "id",property = "userList",
                            many = @Many(select = "com.tr.chat.mapper.GroupUserMapper.getGroupUserByGroupId")),
                    @Result(column = "name",property = "name"),
                    @Result(column = "nick_index",property = "nickIndex"),
                    @Result(column = "avatar",property = "avatar"),
                    @Result(column = "number",property = "number"),
            }
    )
    Group getGroupInfoById(String groupId);
}
