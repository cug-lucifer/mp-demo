package com.itheima.mp.mapper.service;

import com.itheima.mp.domain.po.User;
import com.itheima.mp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class IUserServiceTest {
    @Autowired
    private IUserService userService;

    @Test
    void testSaveUser(){
        User user = new User();
        //user.setId(5L);
        user.setUsername("LiLi");
        user.setPassword("123");
        user.setPhone("18688990015");
        user.setBalance(200);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.save(user);
    }

    @Test
    void testQuery(){
        List<User> users = userService.listByIds(List.of(1L, 2L, 4L));
        users.forEach(System.out::println);
    }

    private User buildUser(int i){
        User user = new User();
        user.setUsername("user_"+i);
        user.setPassword("123");
        user.setPhone(""+(18988190000L+i));
        user.setBalance(2000);
        user.setInfo("{\"age\":24}");
        user.setCreateTime(LocalDateTime.now());
        user.setCreateTime(user.getCreateTime());
        return user;
    }

    @Test
    void testSaveBatch(){
        List<User> users = new ArrayList<>(1000);
        long b = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            users.add(buildUser(i));
            if (i%1000==0){
                userService.saveBatch(users);
                users.clear();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("TimeUsing: "+ (e-b));
    }
}
