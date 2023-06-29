package com.tr.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.tr.chat.entity.GroupUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GroupUserMapper extends BaseMapper<GroupUser> {
    @Select("select * from group_user where group_id=#{groupId}")
    @Results(
            {
                    @Result(column = "user_id",property = "user",
                            many = @Many(select = "com.tr.chat.mapper.UserMapper.getSimUserInfo")),
            }
    )
    List<GroupUser> getGroupUserByGroupId(String groupId);
}
