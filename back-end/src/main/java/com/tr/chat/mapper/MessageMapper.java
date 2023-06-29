package com.tr.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tr.chat.entity.Message;
import com.tr.chat.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    @Select("SELECT * FROM message where (to_user_id=#{toUserId} and from_user_id=#{fromUserId}) or (to_user_id=#{fromUserId} and from_user_id=#{toUserId}) ORDER BY send_time desc")
    @Results(
            {
                    @Result(column = "id",property = "id"),
                    @Result(column = "status",property = "status"),
                    @Result(column = "type",property = "type"),
                    @Result(column = "send_Time",property = "sendTime"),
                    @Result(column = "content",property = "content"),
                    @Result(column = "file_size",property = "fileSize"),
                    @Result(column = "file_name",property = "fileName"),
                    @Result(column = "to_user_id",property = "toUserId"),
                    @Result(column = "to_group_id",property = "toGroupId"),
                    @Result(column = "from_user_id",property = "fromUser",javaType = User.class,
                            one=@One(select = "com.tr.chat.mapper.UserMapper.selectById")
                    )
            }
    )
    Page<Message> getPrivateMsg(String toUserId, String fromUserId, Page<Message> page);


    @Select("SELECT * FROM message where to_group_id=#{toGroupId} ORDER BY send_time desc")
    @Results(
            {
                    @Result(column = "id",property = "id"),
                    @Result(column = "status",property = "status"),
                    @Result(column = "type",property = "type"),
                    @Result(column = "send_Time",property = "sendTime"),
                    @Result(column = "content",property = "content"),
                    @Result(column = "file_size",property = "fileSize"),
                    @Result(column = "file_name",property = "fileName"),
                    @Result(column = "to_user_id",property = "toUserId"),
                    @Result(column = "to_group_id",property = "toGroupId"),
                    @Result(column = "from_user_id",property = "fromUser",javaType = User.class,
                            one=@One(select = "com.tr.chat.mapper.UserMapper.selectById")
                    )
            }
    )
    Page<Message> getGroupMsg(String toGroupId, Page<Message> page);

    @Select("select * from message where id=#{id}")
    @Results(
            {
                    @Result(column = "id",property = "id"),
                    @Result(column = "status",property = "status"),
                    @Result(column = "type",property = "type"),
                    @Result(column = "send_Time",property = "sendTime"),
                    @Result(column = "content",property = "content"),
                    @Result(column = "file_size",property = "fileSize"),
                    @Result(column = "file_name",property = "fileName"),
                    @Result(column = "to_user_id",property = "toUserId"),
                    @Result(column = "to_group_id",property = "toGroupId"),
                    @Result(column = "from_user_id",property = "fromUser",javaType = User.class,
                            one=@One(select = "com.tr.chat.mapper.UserMapper.selectById")
                    )
            }
    )
    Message getById(String id);



    @Select("select type,content,send_time from message where (to_user_id=#{toId} and from_user_id=#{fromId}) or (to_user_id=#{fromId} and from_user_id=#{toId}) order by send_time desc limit 1")
    Message getPrivateLastMsg(String toId,String fromId);

    @Select("select type,content,send_time from message where to_group_id=#{toId} order by send_time desc limit 1")
    Message getGroupLastMsg(String toId);

}
