package com.xiehang.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiehang.client.annotation.RemoteInvoke;
import com.xiehang.client.param.Response;
import com.xiehang.user.model.User;
import com.xiehang.user.remote.UserRemote;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasicService {


    @RemoteInvoke
    private UserRemote userRemote;

    public void testSaveUser() {
        Object response = userRemote.saveUser(new User(1, "谢航"));
        System.out.println(JSONObject.toJSONString(response));
    }

    public void testSaveUsers() {
        User user1 = new User(1, "谢航1");
        User user2 = new User(2, "谢航2");
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        Object resp = userRemote.saveUsers(list);
        System.out.println(JSONObject.toJSONString(resp));
    }

}
