package com.tr.chat.test;

import com.tr.chat.entity.Resp;
import com.tr.chat.mapper.GroupMapper;
import com.tr.chat.mapper.GroupUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@CrossOrigin(origins = "*")
public class TestController {
    private Resp resp;
    private GroupMapper mapper;
    private GroupUserMapper groupUserMapper;

    @Autowired
    public TestController(Resp resp, GroupMapper mapper, GroupUserMapper groupUserMapper) {
        this.resp = resp;
        this.mapper = mapper;
        this.groupUserMapper = groupUserMapper;
    }


    @GetMapping("/test")
    public Resp doGet(@RequestParam Map<Object, String> map, HttpServletRequest request) {
        System.out.println(map);
        resp.success(mapper.getGroupInfoById(map.get("id")),"成功");
        return resp;
    }
}
