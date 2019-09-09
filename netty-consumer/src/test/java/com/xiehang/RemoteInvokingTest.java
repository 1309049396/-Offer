package com.xiehang;

import com.alibaba.fastjson.JSONObject;
import com.xiehang.client.annotation.RemoteInvoke;
import com.xiehang.client.param.Response;
import com.xiehang.user.bean.User;
import com.xiehang.user.remote.UserRemote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoteInvokingTest.class)
@ComponentScan("com")
public class RemoteInvokingTest {

    @RemoteInvoke //动态代理
    private UserRemote userRemote;

    @Test
    public void testSaveUser() {
        Response response = userRemote.saveUser(new User(1, "谢航"));
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void testSaveUsers() {
        User user1 = new User(1, "谢航1");
        User user2 = new User(2, "谢航2");
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        userRemote.saveUsers(list);
    }
}
