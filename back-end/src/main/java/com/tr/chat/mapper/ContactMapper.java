package com.tr.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tr.chat.entity.Contact;
import com.tr.chat.entity.Group;
import com.tr.chat.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ContactMapper extends BaseMapper<Contact> {
    @Select("select * from contact_info where id=#{id}")
    @Results(
            {
                    @Result(column = "id",property = "id"),
                    @Result(column = "user_id",property = "user",javaType = User.class,
                            one=@One(select = "com.tr.chat.mapper.UserMapper.selectById")
                    ),
                    @Result(column = "group_id",property = "group",javaType = Group.class,
                            one=@One(select = "com.tr.chat.mapper.GroupMapper.selectById")
                    ),
                    @Result(column = "unread",property = "unread"),
                    @Result(column = "owner_id",property = "owner",javaType = User.class,
                            one=@One(select = "com.tr.chat.mapper.UserMapper.selectById")
                    )
            }
    )
    Page<Contact> getPageById(String id,Page<Contact> page);

    @Select("select * from contact_info where owner_id=#{id}")
    @Results(
            {
                    @Result(column = "id",property = "id"),
                    @Result(column = "user_id",property = "user",javaType = User.class,
                            one=@One(select = "com.tr.chat.mapper.UserMapper.selectById")
                    ),
                    @Result(column = "group_id",property = "group",javaType = Group.class,
                            one=@One(select = "com.tr.chat.mapper.GroupMapper.selectById")
                    ),
                    @Result(column = "unread",property = "unread"),
                    @Result(column = "owner_id",property = "owner",javaType = User.class,
                            one=@One(select = "com.tr.chat.mapper.UserMapper.selectById")
                    )
            }
    )
    Page<Contact> getPageByOwner(String id,Page<Contact> page);

    @Select("select * from contact_info where owner_id=#{id} and user_id=#{fromId}")
    @Results(
            {
                    @Result(column = "id",property = "id"),
                    @Result(column = "user_id",property = "user",javaType = User.class,
                            one=@One(select = "com.tr.chat.mapper.UserMapper.selectById")
                    ),
                    @Result(column = "group_id",property = "group",javaType = Group.class,
                            one=@One(select = "com.tr.chat.mapper.GroupMapper.selectById")
                    ),
                    @Result(column = "unread",property = "unread"),
                    @Result(column = "owner_id",property = "owner",javaType = User.class,
                            one=@One(select = "com.tr.chat.mapper.UserMapper.selectById")
                    )
            }
    )
    Contact getByOwnerFromUser(String id,String fromId);

    @Select("select * from contact_info where owner_id=#{id} and group_id=#{fromId}")
    @Results(
            {
                    @Result(column = "id",property = "id"),
                    @Result(column = "user_id",property = "user",javaType = User.class,
                            one=@One(select = "com.tr.chat.mapper.UserMapper.selectById")
                    ),
                    @Result(column = "group_id",property = "group",javaType = Group.class,
                            one=@One(select = "com.tr.chat.mapper.GroupMapper.selectById")
                    ),
                    @Result(column = "unread",property = "unread"),
                    @Result(column = "owner_id",property = "owner",javaType = User.class,
                            one=@One(select = "com.tr.chat.mapper.UserMapper.selectById")
                    )
            }
    )
    Contact getByOwnerFromGroup(String id,String fromId);

    @Update("update contact_info set unread=#{unread} where id=#{id}")
    Integer updateContact(String id,String unread);
}
